package com.dburyak.example.jwt.lib.req;

import com.dburyak.example.jwt.lib.req.cfg.FilterOrder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.dburyak.example.jwt.lib.req.Headers.TENANT_UUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


/**
 * Filter to extract tenant IDs and set it in the request attributes. There are two tenant ID parameters for each
 * request:
 * <ul>
 *     <li>Tenant ID of the caller - extracted from {@link Headers#TENANT_UUID} header and overridden by the tenant UUID
 *     from the token if request is authenticated.
 *     <li>Tenant ID of the requested resource - extracted from the {@code tenantUuid} query parameter if present,
 *     otherwise it's assumed that requested resource belongs to the caller's tenant.
 */
@RequiredArgsConstructor
public class TenantUuidExtractionFilter extends OncePerRequestFilter implements Ordered {

    @Getter
    private final int order = FilterOrder.EXTRACT_TENANT.getOrder();

    private final RequestUtil requestUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var callersTenantUuidStr = request.getHeader(TENANT_UUID.getHeader());
        var requestedTenantUuidStr = request.getParameter(Attributes.TENANT_UUID);
        var callersTenantUuid = isNotBlank(callersTenantUuidStr) ? UUID.fromString(callersTenantUuidStr.strip())
                : null;
        var requestedTenantUuid = isNotBlank(requestedTenantUuidStr) ? UUID.fromString(requestedTenantUuidStr.strip())
                : null;
        if (callersTenantUuid != null) {
            requestUtil.setCallersTenantUuid(request, callersTenantUuid);
            requestUtil.setTenantUuid(request, requestedTenantUuid != null ? requestedTenantUuid : callersTenantUuid);
        } else if (requestedTenantUuid != null) {
            // in this case caller's tenantUuid is supposed to be deduced from the auth info
            requestUtil.setTenantUuid(request, requestedTenantUuid);
        }
        filterChain.doFilter(request, response);
    }
}
