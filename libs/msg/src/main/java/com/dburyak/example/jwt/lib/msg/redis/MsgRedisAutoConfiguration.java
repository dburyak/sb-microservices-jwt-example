package com.dburyak.example.jwt.lib.msg.redis;

import com.dburyak.example.jwt.lib.auth.AuthExtractor;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.lib.msg.cfg.Messaging;
import com.dburyak.example.jwt.lib.msg.cfg.MsgAutoConfiguration;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@AutoConfiguration(after = {MsgAutoConfiguration.class, PropsAutoConfiguration.class})
@Conditional(MessagingAndRedisEnabledCondition.class)
public class MsgRedisAutoConfiguration {

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public JedisPool jedisPool(RedisMessagingProperties redisMsgProps, RedisProperties redisMainProps) {
        var poolCfg = new GenericObjectPoolConfig<Jedis>();
        poolCfg.setJmxEnabled(false);
        if (isNotBlank(redisMsgProps.getUrl())) {
            return new JedisPool(poolCfg, redisMsgProps.getUrl());
        } else if (isNotBlank(redisMainProps.getUrl())) {
            return new JedisPool(poolCfg, redisMainProps.getUrl());
        } else {
            return new JedisPool(poolCfg, redisMainProps.getHost(), redisMainProps.getPort());
        }
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    @Redis
    public PointToPointMsgQueue redisMsgQueue(JedisPool jedisPool, @Messaging Executor msgSubscriberExecutor,
            Optional<ServiceTokenManager> serviceTokenManager, RequestUtil requestUtil,
            List<AuthExtractor> authExtractors, AuthenticationManager authManager) {
        return new PointToPointMsgQueueRedisImpl(jedisPool, msgSubscriberExecutor, serviceTokenManager, requestUtil,
                authExtractors, authManager);
    }
}
