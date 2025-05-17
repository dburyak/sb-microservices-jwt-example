package com.dburyak.example.jwt.lib.auth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface SecurityFilterChainHttpConfigurer {
    HttpSecurity configure(HttpSecurity http);
}
