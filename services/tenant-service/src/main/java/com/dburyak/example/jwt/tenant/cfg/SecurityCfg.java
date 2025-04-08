package com.dburyak.example.jwt.tenant.cfg;

import com.dburyak.example.jwt.lib.auth.JwtFilter;
import com.dburyak.example.jwt.lib.req.TenantIdHeaderExtractionFilter;
import com.dburyak.example.jwt.lib.req.TenantIdQueryParamExtractionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS_ROOT;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANT_EXISTS;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.ACTUATOR_READ;
import static com.dburyak.example.jwt.tenant.cfg.Authorities.TENANT_CREATE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChain security(HttpSecurity http, TenantIdHeaderExtractionFilter tenantIdFilter,
            JwtFilter jwtFilter, TenantIdQueryParamExtractionFilter tenantIdQueryParamExtractionFilter)
            throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(GET, "/actuator/**").hasAuthority(ACTUATOR_READ)

                        .requestMatchers(POST, TENANTS_ROOT).hasAuthority(TENANT_CREATE)
                        .requestMatchers(GET, TENANTS_ROOT + TENANT_EXISTS).hasAuthority(Authorities.TENANT_EXISTS)
                        .anyRequest().denyAll())

                .addFilterBefore(tenantIdFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, TenantIdHeaderExtractionFilter.class)
                .addFilterAfter(tenantIdQueryParamExtractionFilter, JwtFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .build();
    }
}
