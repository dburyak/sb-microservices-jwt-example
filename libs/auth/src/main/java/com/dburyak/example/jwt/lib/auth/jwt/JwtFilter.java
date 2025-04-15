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

import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_DEVICE_ID;
import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_TENANT_UUID;

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
                requestUtil.setAuthToken(request, jwtTokenStr);
                // if tenantUuid is set in header, tenantUuid from JWT token always overrides it
                requestUtil.setTenantUuid(request, parsedToken.getTenantUuid());
                requestUtil.setUserUuid(request, parsedToken.getUserUuid());
                requestUtil.setDeviceId(request, parsedToken.getDeviceId());
                var isServiceRequest = SERVICE_TENANT_UUID.equals(parsedToken.getTenantUuid()) &&
                        SERVICE_DEVICE_ID.equals(parsedToken.getDeviceId());
                requestUtil.setServiceRequest(request, isServiceRequest);
            }
        }
        filterChain.doFilter(request, response);
    }
}
