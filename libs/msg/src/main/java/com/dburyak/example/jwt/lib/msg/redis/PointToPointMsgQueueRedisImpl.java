package com.dburyak.example.jwt.lib.msg.redis;

import com.dburyak.example.jwt.lib.auth.AppAuthentication;
import com.dburyak.example.jwt.lib.auth.AuthExtractor;
import com.dburyak.example.jwt.lib.auth.apikey.ApiKeyAuth;
import com.dburyak.example.jwt.lib.auth.jwt.JwtAuth;
import com.dburyak.example.jwt.lib.auth.jwt.JwtServiceTokenManager;
import com.dburyak.example.jwt.lib.msg.Msg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.JedisPool;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.dburyak.example.jwt.lib.req.Headers.API_KEY;
import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_DEVICE_ID;
import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_TENANT_UUID;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static redis.clients.jedis.params.SetParams.setParams;

/**
 * NOTE: implementation needs a better flexibility, using redis, codecs, auth via interfaces, etc. For
 * simplicity, I'll put auth code right here, and use kryo directly with simple mutex and no any performance
 * optimizations.
 */
@Slf4j
public class PointToPointMsgQueueRedisImpl implements PointToPointMsgQueue {
    private static final String DUP_PREFIX = "dup:";
    private static final String DEFAULT_CONSUMER_GROUP = "dfl"; // shorter version
    private static final Duration DUP_LOCK_TTL = Duration.ofHours(1); // this should be configurable
    private static final byte[] NO_DATA = new byte[0];

    private final JedisPool jedisPool;
    private final Executor subscriberExecutor;
    private final JwtServiceTokenManager jwtServiceTokenManager;
    private final RequestUtil requestUtil;
    private final List<AuthExtractor> authExtractors;
    private final AuthenticationManager authManager;

    private final Object subLock = new Object();
    private final Kryo subKryo;
    private final Input subKryoInput;

    private final Object pubLock = new Object();
    private final Kryo pubKryo;
    private final Output pubKryoOutput;

    public PointToPointMsgQueueRedisImpl(JedisPool jedisPool,
            Executor subscriberExecutor,
            Optional<JwtServiceTokenManager> serviceTokenManager,
            RequestUtil requestUtil,
            List<AuthExtractor> authExtractors,
            AuthenticationManager authManager) {
        this.jedisPool = jedisPool;
        this.subscriberExecutor = subscriberExecutor;
        this.jwtServiceTokenManager = serviceTokenManager.orElse(null);
        this.requestUtil = requestUtil;
        this.authExtractors = authExtractors;
        this.authManager = authManager;
        subKryo = new Kryo();
        // UUID serializer is not registered by default, have to do it manually
        subKryo.addDefaultSerializer(UUID.class, new DefaultSerializers.UUIDSerializer());
        subKryo.setRegistrationRequired(false);
        subKryo.setDefaultSerializer(new SerializerFactory.FieldSerializerFactory());
        subKryoInput = new Input();
        pubKryo = new Kryo();
        pubKryo.addDefaultSerializer(UUID.class, new DefaultSerializers.UUIDSerializer());
        pubKryo.setRegistrationRequired(false);
        pubKryo.setDefaultSerializer(new SerializerFactory.FieldSerializerFactory());
        pubKryoOutput = new Output(1024, -1);
    }

    @Override
    public <T> void publish(String topic, T msg) {
        var msgId = UUID.randomUUID();
        var headers = buildHeaders();
        var msgBytes = serialize(msgId, headers, msg);
        try (var jedis = jedisPool.getResource()) {
            jedis.publish(topic.getBytes(UTF_8), msgBytes);
        }
    }

    @Override
    public <T> void subscribe(String topic, Predicate<AppAuthentication> access, Consumer<Msg<T>> handler) {
        // with a shorter version of the default consumer group to save few bytes in redis
        subscribe(topic, DEFAULT_CONSUMER_GROUP, access, handler);
    }

    @Override
    public <T> void subscribe(String topic, String consumerGroup, Predicate<AppAuthentication> access,
            Consumer<Msg<T>> handler) {
        subscriberExecutor.execute(() -> {
            try (var jedis = jedisPool.getResource()) {
                var subscriber = new BinaryJedisPubSub() {
                    @Override
                    public void onMessage(byte[] topic, byte[] msgBytes) {
                        MsgRedisImpl<T> unauthenticatedMsg = deserialize(msgBytes);
                        var msg = authenticate(unauthenticatedMsg);
                        if (msg == null) {
                            log.warn("received msg with bad auth: topic={}, msg={}", topic, unauthenticatedMsg);
                            return;
                        }
                        var isAllowed = access.test(msg.getAuth());
                        if (!isAllowed) {
                            log.warn("received msg with not enough privileges: topic={}, msg={}", topic, msg);
                            return;
                        }
                        var dupKey = DUP_PREFIX + consumerGroup + msg.getMsgId();
                        var ifNotExistsAndWithTtl = setParams().nx().ex(DUP_LOCK_TTL.toSeconds());
                        var dupKeyBytes = dupKey.getBytes(UTF_8);
                        var isDup = false;
                        try (var jedisForCmd = jedisPool.getResource()) {
                            isDup = (jedisForCmd.set(dupKeyBytes, NO_DATA, ifNotExistsAndWithTtl) == null);
                        }
                        if (!isDup) {
                            subscriberExecutor.execute(() -> {
                                try {
                                    populateRequestCtx(msg.getAuth());
                                    handler.accept(msg);
                                    clearRequestCtx();
                                    msg.ack();
                                } catch (Throwable anyErr) {
                                    clearRequestCtx();
                                    if (isRetryable(anyErr)) {
                                        try (var jedisForCmd = jedisPool.getResource()) {
                                            jedisForCmd.del(dupKeyBytes);
                                        }
                                    }
                                    msg.nack();
                                }
                            });
                        }
                    }
                };
                jedis.subscribe(subscriber, topic.getBytes(UTF_8));
            }
        });
    }

    private <T> byte[] serialize(UUID msgId, Map<String, String> headers, T msg) {
        synchronized (pubLock) {
            pubKryoOutput.reset();
            pubKryo.writeObject(pubKryoOutput, msgId);
            pubKryo.writeObject(pubKryoOutput, headers.size());
            for (var entry : headers.entrySet()) {
                pubKryo.writeObject(pubKryoOutput, entry.getKey());
                pubKryo.writeObject(pubKryoOutput, entry.getValue());
            }
            pubKryo.writeClassAndObject(pubKryoOutput, msg);
            var size = pubKryoOutput.position();
            var msgBytes = new byte[size];
            System.arraycopy(pubKryoOutput.getBuffer(), 0, msgBytes, 0, size);
            return msgBytes;
        }
    }

    private <T> MsgRedisImpl<T> deserialize(byte[] msgBytes) {
        synchronized (subLock) {
            subKryoInput.setBuffer(msgBytes);
            var msgId = subKryo.readObject(subKryoInput, UUID.class);
            var headersSize = subKryo.readObject(subKryoInput, Integer.class);
            Map<String, String> headers = (headersSize > 0) ? new HashMap<>() : emptyMap();
            for (int i = 0; i < headersSize; i++) {
                var key = subKryo.readObject(subKryoInput, String.class);
                var value = subKryo.readObject(subKryoInput, String.class);
                headers.put(key, value);
            }
            var msgData = (T) subKryo.readClassAndObject(subKryoInput);
            return new MsgRedisImpl<>(msgId, headers, msgData);
        }
    }

    private Map<String, String> buildHeaders() {
        var headers = new HashMap<String, String>();
        var authToken = requestUtil.getAuthToken();
        if (isNotBlank(authToken)) {
            headers.put(AUTHORIZATION, "Bearer " + authToken);
        } else {
            var apiKey = requestUtil.getApiKey();
            if (isNotBlank(apiKey)) {
                headers.put(API_KEY.getHeader(), apiKey);
            } else if (jwtServiceTokenManager != null) {
                return Map.of(AUTHORIZATION, "Bearer " + jwtServiceTokenManager.getServiceToken());
            }
        }
        // it's safer to forbid unauthenticated messages, there are no realistic use cases for this anyway
        throw new IllegalArgumentException("auth information is not found");
    }

    private <T> MsgRedisImpl<T> authenticate(MsgRedisImpl<T> unauthenticatedMsg) {
        var auth = authExtractors.stream()
                .map(e -> e.extract(unauthenticatedMsg.getHeaders()))
                .filter(Objects::nonNull)
                .map(authManager::authenticate)
                .filter(a -> a != null && a.isAuthenticated())
                .findFirst().orElse(null);
        if (auth instanceof AppAuthentication appAuth) {
            return unauthenticatedMsg.withAuth(appAuth);
        } else {
            return null;
        }
    }

    private void populateRequestCtx(AppAuthentication auth) {
        SecurityContextHolder.getContext().setAuthentication(auth);
        // ugly, but it's not worth the effort to redesign
        if (auth instanceof JwtAuth jwtAuth) {
            requestUtil.setAuthToken(null, jwtAuth.getJwtToken());
        } else if (auth instanceof ApiKeyAuth apiKeyAuth) {
            requestUtil.setApiKey(null, apiKeyAuth.getApiKey());
        }
        requestUtil.setTenantUuid(null, auth.getTenantUuid());
        requestUtil.setUserUuid(null, auth.getUserUuid());
        requestUtil.setDeviceId(null, auth.getDeviceId());
        var isServiceRequest = SERVICE_TENANT_UUID.equals(auth.getTenantUuid()) &&
                SERVICE_DEVICE_ID.equals(auth.getDeviceId());
        requestUtil.setServiceRequest(null, isServiceRequest);
    }

    private void clearRequestCtx() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    private boolean isRetryable(Throwable err) {
        if (err instanceof IllegalArgumentException) {
            return false;
        }
        return true;
    }
}
