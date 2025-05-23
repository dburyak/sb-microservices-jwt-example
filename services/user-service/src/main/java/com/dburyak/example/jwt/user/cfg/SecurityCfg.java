package com.dburyak.example.jwt.user.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import com.dburyak.example.jwt.lib.auth.jwt.JwtFilter;
import com.dburyak.example.jwt.lib.req.TenantUuidExtractionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.dburyak.example.jwt.api.internal.tenant.Paths.PATH_TENANT_EXISTS_BY_NAME;
import static com.dburyak.example.jwt.api.internal.tenant.Paths.PATH_TENANT_EXISTS_BY_UUID;
import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_NAME;
import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_UUID;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS;
import static com.dburyak.example.jwt.api.user.Paths.USERS;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.ACTUATOR_READ;
import static com.dburyak.example.jwt.user.cfg.Authorities.USER_READ;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChain security(HttpSecurity http, TenantUuidExtractionFilter tenantUuidFilter,
            JwtFilter jwtFilter)
            throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(GET, "/actuator/**").hasAuthority(ACTUATOR_READ)

                        .requestMatchers(POST, USERS).permitAll()
                        .requestMatchers(GET, USERS).hasAuthority(USER_READ)
                        .anyRequest().denyAll())

                .addFilterBefore(tenantUuidFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, TenantUuidExtractionFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .build();
    }

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
