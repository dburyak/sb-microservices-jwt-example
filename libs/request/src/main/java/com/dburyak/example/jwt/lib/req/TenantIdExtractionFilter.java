package com.dburyak.example.jwt.lib.req;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dburyak.example.jwt.lib.req.Headers.TENANT_ID;


/**
 * Filter to extract tenant ID from the request header and set it in the request attributes.
 * This is needed only for unprotected endpoints. In protected endpoints, the tenant ID is extracted from the JWT token.
 */
@RequiredArgsConstructor
public class TenantIdExtractionFilter extends OncePerRequestFilter {
    private final RequestUtil requestUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var tenantId = request.getHeader(TENANT_ID.getHeader());
        if (StringUtils.isNotBlank(tenantId)) {
            requestUtil.setTenantId(request, tenantId);
        }
        filterChain.doFilter(request, response);
    }
}
