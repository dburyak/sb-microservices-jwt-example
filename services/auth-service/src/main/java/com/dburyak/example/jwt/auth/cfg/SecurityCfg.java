package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_REFRESH;
import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_TOKEN;
import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_WRITE;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg() {
        return auth -> auth
                // anyone can authenticate
                .requestMatchers(POST, PATH_AUTH_JWT_TOKEN, PATH_AUTH_JWT_REFRESH).permitAll()

                .requestMatchers(POST, USERS).hasAuthority(USER_WRITE);
    }
}
