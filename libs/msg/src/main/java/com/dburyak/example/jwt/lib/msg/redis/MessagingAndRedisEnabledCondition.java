package com.dburyak.example.jwt.lib.msg.redis;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import static org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION;

public class MessagingAndRedisEnabledCondition extends AllNestedConditions {

    public MessagingAndRedisEnabledCondition() {
        super(PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(prefix = "msg", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class MessagingEnabledCondition {
    }

    @ConditionalOnProperty(prefix = "msg.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class RedisMessagingEnabledCondition {
    }
}
