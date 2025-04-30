package com.dburyak.example.jwt.api.internal.user.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties({UserMsgProperties.class, UserServiceClientProperties.class})
public class PropsAutoConfiguration {
}
