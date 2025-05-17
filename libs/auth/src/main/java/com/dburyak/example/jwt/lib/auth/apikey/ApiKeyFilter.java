package com.dburyak.example.jwt.lib.auth.apikey;

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

import static com.dburyak.example.jwt.lib.req.Headers.API_KEY;

@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter implements Ordered {
    private final ApiKeyAuthExtractor apiKeyExtractor;
    private final RequestUtil requestUtil;
    private final AuthenticationManager autManager;

    @Getter
    private final int order = FilterOrder.APIKEY_AUTH.getOrder();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null && currentAuth.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        var relevantHeaders = Map.of(API_KEY.getHeader(), request.getHeader(API_KEY.getHeader()));
        var apiKey = apiKeyExtractor.extract(relevantHeaders);
        if (apiKey != null) {
            SecurityContextHolder.getContext().setAuthentication(apiKey);
            var authenticated = autManager.authenticate(apiKey);
            if (authenticated instanceof ApiKeyAuth authenticatedApiKey) { // != null
                SecurityContextHolder.getContext().setAuthentication(authenticatedApiKey);
                requestUtil.setApiKey(request, authenticatedApiKey.getApiKey());
                // if tenantUuid is set in header, tenantUuid that apiKey belongs to always overrides it
                requestUtil.setTenantUuid(request, authenticatedApiKey.getTenantUuid());
                requestUtil.setUserUuid(request, authenticatedApiKey.getUserUuid());
                requestUtil.setDeviceId(request, authenticatedApiKey.getDeviceId());
                // service-to-service communications never use api-keys, only jwt service tokens
                requestUtil.setServiceRequest(request, false);
            }
        }
        filterChain.doFilter(request, response);
    }
}
