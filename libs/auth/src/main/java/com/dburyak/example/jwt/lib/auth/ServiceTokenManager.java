package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import static com.dburyak.example.jwt.lib.auth.AuthConstants.SERVICE_UUIDS;
import static com.dburyak.example.jwt.lib.auth.Role.SERVICE;

public class ServiceTokenManager {
    private static final Set<String> SERVICE_ROLES = Set.of(SERVICE.getName());
    private static final Duration RENEWAL_GAP = Duration.ofSeconds(30);
    private final JwtGenerator jwtGenerator;
    private final UUID serviceUuid;
    private final Duration ttl;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private String token;
    private Instant expiresAt = Instant.EPOCH;

    public ServiceTokenManager(JwtAuthProperties props, JwtGenerator jwtGenerator, String serviceName) {
        this.jwtGenerator = jwtGenerator;
        this.serviceUuid = SERVICE_UUIDS.get(serviceName);
        this.ttl = props.getGenerator().getTtl().minus(RENEWAL_GAP);
    }

    public String getServiceToken() {
        var t = withReadLock(() -> !isTokenExpired() ? token : null);
        if (t == null) {
            t = withWriteLock(() -> {
                if (isTokenExpired()) {
                    expiresAt = Instant.now().plus(ttl);
                    token = jwtGenerator.generateServiceToken(serviceUuid, SERVICE_ROLES);
                }
                return token;
            });
        }
        return t;
    }

    private boolean isTokenExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    private <T> T withReadLock(Supplier<T> action) {
        try {
            lock.readLock().lock();
            return action.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    private <T> T withWriteLock(Supplier<T> action) {
        try {
            lock.writeLock().lock();
            return action.get();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
