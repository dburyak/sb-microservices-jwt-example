package com.dburyak.example.jwt.lib.auth.apikey;

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

import static com.dburyak.example.jwt.lib.req.Headers.API_KEY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {
    private final RequestUtil requestUtil;
    private final AuthenticationManager autManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var apiKey = request.getHeader(API_KEY.getHeader());
        if (isNotBlank(apiKey)) {
            var auth = new ApiKeyAuth(apiKey);
            var authWithAuthorities = autManager.authenticate(auth);
            if (authWithAuthorities instanceof ApiKeyAuth apiKeyAuth) {
                SecurityContextHolder.getContext().setAuthentication(apiKeyAuth);
                requestUtil.setApiKey(apiKey);
                requestUtil.setTenantUuid(request, apiKeyAuth.getTenantUuid());
                requestUtil.setUserUuid(request, apiKeyAuth.getUserUuid());
                requestUtil.setDeviceId(request, apiKeyAuth.getDeviceId());
                requestUtil.setServiceRequest(request, false);
            }
        }
        filterChain.doFilter(request, response);
    }
}
