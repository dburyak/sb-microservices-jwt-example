package com.dburyak.example.jwt.lib.req;

import com.dburyak.example.jwt.lib.req.cfg.FilterOrder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dburyak.example.jwt.lib.req.Headers.TENANT_UUID;

/**
 * Filter to verify that the tenant of the request is identified. We block requests that do not have tenant information
 * associated with them.
 */
@RequiredArgsConstructor
public class TenantVerificationFilter extends OncePerRequestFilter implements Ordered {

    @Getter
    private final int order = FilterOrder.VERIFY_TENANT.getOrder();

    private final RequestUtil requestUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (requestUtil.getCallersTenantUuid(request) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write("Tenant UUID is not specified in \"" + TENANT_UUID.getHeader() + "\" header.");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
