package com.dburyak.example.jwt.tenant.cfg;

import com.dburyak.example.jwt.lib.auth.jwt.JwtFilter;
import com.dburyak.example.jwt.lib.req.TenantUuidHeaderExtractionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS_ROOT;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANT_EXISTS;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.ACTUATOR_READ;
import static com.dburyak.example.jwt.tenant.cfg.Authorities.TENANT_MANAGE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChain security(HttpSecurity http, TenantUuidHeaderExtractionFilter tenantUuidFilter,
            JwtFilter jwtFilter) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(GET, "/actuator/**").hasAuthority(ACTUATOR_READ)

                        .requestMatchers(POST, TENANTS_ROOT).hasAuthority(TENANT_MANAGE)
                        .requestMatchers(DELETE, TENANTS_ROOT).hasAuthority(TENANT_MANAGE)
                        .requestMatchers(GET, TENANTS_ROOT + "/*").hasAuthority(TENANT_MANAGE)
                        .requestMatchers(GET, TENANTS_ROOT + "/*" + TENANT_EXISTS).authenticated()
                        .anyRequest().denyAll())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tenantUuidFilter, JwtFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .build();
    }
}
