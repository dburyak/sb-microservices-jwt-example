package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_REFRESH;
import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_ROOT;
import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_TOKEN;
import static com.dburyak.example.jwt.api.internal.auth.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_WRITE;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg() {
        return auth -> auth
                // anyone can authenticate
                .requestMatchers(POST, AUTH_JWT_ROOT + AUTH_JWT_TOKEN, AUTH_JWT_ROOT + AUTH_JWT_REFRESH).permitAll()
                .requestMatchers(POST, USERS_ROOT).hasAuthority(USER_WRITE);
    }
}
