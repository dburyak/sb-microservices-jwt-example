package com.dburyak.example.jwt.api.internal.tenant.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties({TenantServiceClientProperties.class, TenantMsgProperties.class})
public class TenantApiInternalPropsAutoConfiguration {
}
