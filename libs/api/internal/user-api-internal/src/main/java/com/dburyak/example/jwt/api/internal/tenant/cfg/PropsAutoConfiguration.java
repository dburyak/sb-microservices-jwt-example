package com.dburyak.example.jwt.api.internal.tenant.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(UserMsgProperties.class)
public class PropsAutoConfiguration {
}
