package com.dburyak.example.jwt.user.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_REGISTRATION_OTP;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg() {
        // @formatter:off
        return auth -> auth
                .requestMatchers(POST, PATH_USER_REGISTRATION_OTP).permitAll()
                .requestMatchers(POST, USERS).permitAll();
        // @formatter:on
    }
}
