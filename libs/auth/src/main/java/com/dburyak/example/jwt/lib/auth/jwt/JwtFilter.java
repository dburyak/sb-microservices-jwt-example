package com.dburyak.example.jwt.lib.auth.jwt;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.dburyak.example.jwt.lib.req.cfg.FilterOrder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_DEVICE_ID;
import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_TENANT_UUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter implements Ordered {
    private final JwtAuthExtractor jwtExtractor;
    private final AuthenticationManager authManager;
    private final RequestUtil requestUtil;

    @Getter
    private final int order = FilterOrder.JWT_AUTH.getOrder();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null && currentAuth.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        var authHeaderValue = request.getHeader(AUTHORIZATION);
        Map<String, String> relevantHeaders = isNotBlank(authHeaderValue)
                ? Map.of(AUTHORIZATION, request.getHeader(AUTHORIZATION))
                : Map.of();
        var jwtAuth = jwtExtractor.extract(relevantHeaders);
        if (jwtAuth != null) {
            var authenticated = authManager.authenticate(jwtAuth);
            if (authenticated instanceof JwtAuth authenticatedJwtAuth) {
                SecurityContextHolder.getContext().setAuthentication(authenticatedJwtAuth);
                requestUtil.setAuthToken(request, authenticatedJwtAuth.getJwtToken());
                // even if tenantUuid is set in header, tenantUuid from JWT token always overrides it
                requestUtil.setCallersTenantUuid(request, authenticatedJwtAuth.getTenantUuid());
                if (requestUtil.getTenantUuid() == null) {
                    // if no "requested tenantUuid" is specified explicitly, assume that the requested resource
                    // belongs to the caller's tenant
                    requestUtil.setTenantUuid(request, authenticatedJwtAuth.getTenantUuid());
                }
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
