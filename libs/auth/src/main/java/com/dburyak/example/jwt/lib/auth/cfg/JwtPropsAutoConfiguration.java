package com.dburyak.example.jwt.lib.auth.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(JwtAuthProperties.class)
public class JwtPropsAutoConfiguration {
    // no beans, just enable configuration properties
}
