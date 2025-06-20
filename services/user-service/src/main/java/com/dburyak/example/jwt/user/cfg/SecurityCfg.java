package com.dburyak.example.jwt.user.cfg;

import com.dburyak.example.jwt.lib.auth.AuthZFactory;
import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_MANAGEMENT;
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_MANAGEMENT_BY_USERNAME;
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_MANAGEMENT_BY_UUID;
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_REGISTRATION_OTP;
import static com.dburyak.example.jwt.user.cfg.Authorities.USER_MANAGE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg(AuthZFactory requires) {
        // @formatter:off
        return auth -> auth
                .requestMatchers(POST, PATH_USER_REGISTRATION_OTP).permitAll()
                .requestMatchers(POST, USERS).permitAll()
                .requestMatchers(GET, PATH_USER_MANAGEMENT_BY_USERNAME).access(requires.authority(USER_MANAGE))
                .requestMatchers(POST, PATH_USER_MANAGEMENT).access(requires.authority(USER_MANAGE))
                .requestMatchers(DELETE, PATH_USER_MANAGEMENT_BY_UUID, PATH_USER_MANAGEMENT_BY_USERNAME)
                    .access(requires.authority(USER_MANAGE))
                ;
        // @formatter:on
    }
}
