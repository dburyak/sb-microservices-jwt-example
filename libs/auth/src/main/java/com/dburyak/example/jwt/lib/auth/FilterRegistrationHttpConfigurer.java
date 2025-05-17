package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.req.TenantVerificationFilter;
import jakarta.servlet.Filter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@RequiredArgsConstructor
public class FilterRegistrationHttpConfigurer implements SecurityFilterChainHttpConfigurer, Ordered {
    private final Filter filter;

    @Getter
    private final int order;

    @Override
    public HttpSecurity configure(HttpSecurity http) {
        return http.addFilterBefore(filter, TenantVerificationFilter.class);
    }
}
