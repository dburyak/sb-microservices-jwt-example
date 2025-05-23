package com.dburyak.example.jwt.tenant.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.internal.tenant.Paths.PATH_TENANT_EXISTS_BY_NAME;
import static com.dburyak.example.jwt.api.internal.tenant.Paths.PATH_TENANT_EXISTS_BY_UUID;
import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_NAME;
import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_UUID;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS;
import static com.dburyak.example.jwt.tenant.cfg.Authorities.TENANT_EXISTS;
import static com.dburyak.example.jwt.tenant.cfg.Authorities.TENANT_MANAGE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg() {
        // @formatter:off
        return auth -> auth
                .requestMatchers(POST, TENANTS).hasAuthority(TENANT_MANAGE)
                .requestMatchers(DELETE, PATH_TENANT_BY_UUID).hasAuthority(TENANT_MANAGE)
                .requestMatchers(GET, PATH_TENANT_BY_UUID, PATH_TENANT_BY_NAME).hasAuthority(TENANT_MANAGE)
                .requestMatchers(GET, PATH_TENANT_EXISTS_BY_UUID, PATH_TENANT_EXISTS_BY_NAME).hasAuthority(TENANT_EXISTS);
        // @formatter:on
    }
}
