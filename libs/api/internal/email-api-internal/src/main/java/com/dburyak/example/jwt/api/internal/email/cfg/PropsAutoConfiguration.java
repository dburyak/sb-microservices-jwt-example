package com.dburyak.example.jwt.api.internal.email.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties({EmailMsgProperties.class})
public class PropsAutoConfiguration {
}
