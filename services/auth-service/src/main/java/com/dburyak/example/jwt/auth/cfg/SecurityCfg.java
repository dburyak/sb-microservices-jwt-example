package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.JwtFilter;
import com.dburyak.example.jwt.lib.req.TenantIdHeaderExtractionFilter;
import com.dburyak.example.jwt.lib.req.TenantIdQueryParamExtractionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.dburyak.example.jwt.api.internal.auth.Paths.USER_REGISTRATION_ROOT;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_WRITE;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.ACTUATOR_READ;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChain jwtAuthFilterChain(HttpSecurity http, TenantIdHeaderExtractionFilter tenantIdFilter,
            JwtFilter jwtFilter, TenantIdQueryParamExtractionFilter tenantIdQueryParamExtractionFilter)
            throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(GET, "/actuator/**").hasAuthority(ACTUATOR_READ.name())

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(POST, USER_REGISTRATION_ROOT).hasAuthority(USER_WRITE.name())
                        .anyRequest().denyAll())

                .addFilterBefore(tenantIdFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, TenantIdHeaderExtractionFilter.class)
                .addFilterAfter(tenantIdQueryParamExtractionFilter, JwtFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .build();
    }
}
