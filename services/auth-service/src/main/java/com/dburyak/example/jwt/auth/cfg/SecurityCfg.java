package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.AuthZFactory;
import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_REFRESH;
import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_TOKEN;
import static com.dburyak.example.jwt.api.auth.Paths.PATH_USER_PASSWORD_RESET;
import static com.dburyak.example.jwt.api.auth.Paths.PATH_USER_PASSWORD_RESET_OTP;
import static com.dburyak.example.jwt.api.common.Paths.PATH_USER_BY_UUID;
import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_ADM_READ;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_ADM_WRITE;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_ALLT_READ;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_ALLT_WRITE;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_READ;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_WRITE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg(AuthZFactory requires) {
        return auth -> auth
                // (in a real world setup, these anonymous auth endpoints should be rate limited)
                // anyone can authenticate
                .requestMatchers(POST, PATH_AUTH_JWT_TOKEN, PATH_AUTH_JWT_REFRESH).permitAll()
                // anyone can attempt to reset password (forgot password flow)
                .requestMatchers(POST, PATH_USER_PASSWORD_RESET_OTP).permitAll()
                .requestMatchers(PUT, PATH_USER_PASSWORD_RESET).permitAll()

                .requestMatchers(POST, USERS).access(requires.anyOf(USER_WRITE, USER_ADM_WRITE, USER_ALLT_WRITE))
                .requestMatchers(GET, PATH_USER_BY_UUID).access(requires.anyOf(USER_READ, USER_ADM_READ, USER_ALLT_READ))
                ;
    }
}
