package com.dburyak.example.jwt.lib.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtParser jwtParser;
    private final AuthenticationManager authManager;
    private final RequestUtil requestUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var jwtTokenStr = authHeader.substring(7);
            var parsedToken = jwtParser.parse(jwtTokenStr);
            if (parsedToken != null) {
                var tokenWithAuthorities = authManager.authenticate(parsedToken);
                SecurityContextHolder.getContext().setAuthentication(tokenWithAuthorities);
                // if tenantId is set in header, tenantId from JWT token always takes precedence
                requestUtil.setTenantId(request, parsedToken.getTenantId());
                requestUtil.setUserUuid(request, parsedToken.getUserUuid());
                requestUtil.setDeviceId(request, parsedToken.getDeviceId());
            }
        }
        filterChain.doFilter(request, response);
    }
}
