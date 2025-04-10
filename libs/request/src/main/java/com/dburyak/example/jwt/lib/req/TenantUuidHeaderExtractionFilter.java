package com.dburyak.example.jwt.lib.req;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.dburyak.example.jwt.lib.req.Headers.TENANT_UUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


/**
 * Filter to extract tenant ID from the request header and set it in the request attributes.
 * This is needed only for unprotected endpoints. In protected endpoints, the tenant ID is extracted from the JWT token.
 */
@RequiredArgsConstructor
public class TenantUuidHeaderExtractionFilter extends OncePerRequestFilter {
    private final RequestUtil requestUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var tenantUuid = request.getHeader(TENANT_UUID.getHeader());
        if (isNotBlank(tenantUuid)) {
            requestUtil.setTenantUuid(request, UUID.fromString(tenantUuid.strip()));
        }
        filterChain.doFilter(request, response);
    }
}
