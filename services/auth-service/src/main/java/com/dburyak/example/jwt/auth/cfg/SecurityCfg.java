package com.dburyak.example.jwt.auth.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static com.dburyak.example.jwt.auth.cfg.Authorities.ACTUATOR_READ;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_CREATE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChain jwtAuthFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(GET, "/actuator/**").hasAuthority(ACTUATOR_READ.name())
                        .requestMatchers(POST, "/user").hasAuthority(USER_CREATE.name())
                        .anyRequest().denyAll())
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .build();
    }
}
