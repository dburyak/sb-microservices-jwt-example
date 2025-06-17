package com.dburyak.example.jwt.tenant.cfg;

import com.dburyak.example.jwt.lib.auth.AuthZFactory;
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
import static com.dburyak.example.jwt.tenant.cfg.Authorities.TENANT_READ;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg(AuthZFactory requires) {
        // @formatter:off
        return auth -> auth
                .requestMatchers(POST, TENANTS).access(requires.authority(TENANT_MANAGE))
                .requestMatchers(DELETE, PATH_TENANT_BY_UUID, PATH_TENANT_BY_NAME).access(requires.authority(TENANT_MANAGE))
                .requestMatchers(GET, PATH_TENANT_BY_UUID, PATH_TENANT_BY_NAME).access(
                        requires.anyOf(TENANT_MANAGE, TENANT_READ))
                .requestMatchers(GET, PATH_TENANT_EXISTS_BY_UUID, PATH_TENANT_EXISTS_BY_NAME).access(
                        requires.anyOf(TENANT_MANAGE, TENANT_READ, TENANT_EXISTS));
        // @formatter:on
    }
}
