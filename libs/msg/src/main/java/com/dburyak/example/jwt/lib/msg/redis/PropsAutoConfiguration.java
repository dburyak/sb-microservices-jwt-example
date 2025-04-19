package com.dburyak.example.jwt.lib.msg.redis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(RedisMessagingProperties.class)
public class PropsAutoConfiguration {
}
