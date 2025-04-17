package com.dburyak.example.jwt.user.cfg;

import com.dburyak.example.jwt.api.internal.tenant.cfg.TenantMsgProperties;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.lib.msg.redis.PointToPointMsgQueueRedisImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Configuration
public class MsgCfg {

    @Bean
    @TenantCreated
    @ConditionalOnProperty(prefix = "msg.tenant.topics.tenant-created", name = "transport", havingValue = "redis")
    public PointToPointMsgQueue tenantCreatedQueue(TenantMsgProperties props) {
        new PointToPointMsgQueueRedisImpl();
    }

    @Bean
    @TenantDeleted
    @ConditionalOnProperty(prefix = "msg.tenant.topics.tenant-deleted", name = "transport", havingValue = "redis")
    public PointToPointMsgQueue tenantDeletedQueue(TenantMsgProperties props) {
        new PointToPointMsgQueueRedisImpl();
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface TenantCreated {}

    @Qualifier
    @Retention(RUNTIME)
    public @interface TenantDeleted {}
}
