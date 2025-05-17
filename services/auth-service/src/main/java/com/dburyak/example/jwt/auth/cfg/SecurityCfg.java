package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.internal.auth.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_WRITE;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg() {
        return auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(POST, USERS_ROOT).hasAuthority(USER_WRITE);
    }
}
