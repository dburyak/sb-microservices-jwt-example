package com.dburyak.example.jwt.api.internal.auth.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(RestClientProperties.class)
public class RestClientAutoConfiguration {
}
