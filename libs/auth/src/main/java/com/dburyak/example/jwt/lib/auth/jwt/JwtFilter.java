package com.dburyak.example.jwt.lib.auth.jwt;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_DEVICE_ID;
import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_TENANT_UUID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtAuthExtractor jwtExtractor;
    private final AuthenticationManager authManager;
    private final RequestUtil requestUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null && currentAuth.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        var relevantHeaders = Map.of(AUTHORIZATION, request.getHeader(AUTHORIZATION));
        var jwtAuth = jwtExtractor.extract(relevantHeaders);
        if (jwtAuth != null) {
            var authenticated = authManager.authenticate(jwtAuth);
            if (authenticated instanceof JwtAuth authenticatedJwtAuth) {
                SecurityContextHolder.getContext().setAuthentication(authenticatedJwtAuth);
                requestUtil.setAuthToken(request, authenticatedJwtAuth.getJwtToken());
                // if tenantUuid is set in header, tenantUuid from JWT token always overrides it
                requestUtil.setTenantUuid(request, authenticatedJwtAuth.getTenantUuid());
                requestUtil.setUserUuid(request, authenticatedJwtAuth.getUserUuid());
                requestUtil.setDeviceId(request, authenticatedJwtAuth.getDeviceId());
                var isServiceRequest = SERVICE_TENANT_UUID.equals(authenticatedJwtAuth.getTenantUuid()) &&
                        SERVICE_DEVICE_ID.equals(authenticatedJwtAuth.getDeviceId());
                requestUtil.setServiceRequest(request, isServiceRequest);
            }
        }
        filterChain.doFilter(request, response);
    }
}
