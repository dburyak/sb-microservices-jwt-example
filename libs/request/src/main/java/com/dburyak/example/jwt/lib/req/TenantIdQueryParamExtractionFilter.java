package com.dburyak.example.jwt.lib.req;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dburyak.example.jwt.lib.req.Attributes.DEVICE_ID;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_ID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Filter to extract tenant ID from the request query parameter and set it in the request attributes.
 * This is needed to be able to override tenantID in the JWT token, which is the use case for service initiated
 * requests.
 */
@RequiredArgsConstructor
public class TenantIdQueryParamExtractionFilter extends OncePerRequestFilter {
    private final RequestUtil requestUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (requestUtil.isServiceRequest()) {
            var tenantId = request.getParameter(TENANT_ID);
            if (isNotBlank(tenantId)) {
                requestUtil.setTenantId(request, tenantId);
            }
            var deviceId = request.getParameter(DEVICE_ID);
            if (isNotBlank(tenantId)) {
                requestUtil.setDeviceId(request, deviceId);
            }
        }
        filterChain.doFilter(request, response);
    }
}
