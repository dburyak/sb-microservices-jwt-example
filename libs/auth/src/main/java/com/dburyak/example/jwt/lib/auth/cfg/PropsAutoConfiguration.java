package com.dburyak.example.jwt.lib.auth.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties({JwtAuthProperties.class, ApiKeyAuthProperties.class})
public class PropsAutoConfiguration {
    // no beans, just enable configuration properties
}
