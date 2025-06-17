package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_REFRESH;
import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_TOKEN;
import static com.dburyak.example.jwt.api.auth.Paths.PATH_USER_PASSWORD_RESET;
import static com.dburyak.example.jwt.api.auth.Paths.PATH_USER_PASSWORD_RESET_OTP;
import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_WRITE;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.ADMIN;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.SA;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg() {
        return auth -> auth
                // (in a real world setup, these anonymous auth endpoints should be rate limited)
                // anyone can authenticate
                .requestMatchers(POST, PATH_AUTH_JWT_TOKEN, PATH_AUTH_JWT_REFRESH).permitAll()
                // anyone can attempt to reset password (forgot password flow)
                .requestMatchers(POST, PATH_USER_PASSWORD_RESET_OTP).permitAll()
                .requestMatchers(PUT, PATH_USER_PASSWORD_RESET).permitAll()

                .requestMatchers(POST, USERS).hasAnyAuthority(SA, ADMIN, USER_WRITE);
    }
}
