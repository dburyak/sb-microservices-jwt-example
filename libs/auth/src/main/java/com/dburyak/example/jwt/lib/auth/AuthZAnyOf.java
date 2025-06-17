package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Set;
import java.util.function.Supplier;

import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.ADMIN;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.SA;
import static org.apache.commons.collections4.SetUtils.intersection;

/**
 * "Any of" auth manager that takes into account tenants data separation and admin and super-admin roles.
 */
@Slf4j
public class AuthZAnyOf implements AuthorizationManager<RequestAuthorizationContext> {
    private final RequestUtil requestUtil;
    private final Set<String> anyOfAuthoritiesRequired;

    public AuthZAnyOf(RequestUtil requestUtil, String[] anyOfAuthoritiesRequired) {
        this.requestUtil = requestUtil;
        this.anyOfAuthoritiesRequired = Set.of(anyOfAuthoritiesRequired);
    }

    public AuthZAnyOf(RequestUtil requestUtil, String authority) {
        this.requestUtil = requestUtil;
        this.anyOfAuthoritiesRequired = Set.of(authority);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext ctx) {
        var authNGeneric = authentication.get();
        if (!(authNGeneric instanceof AppAuthentication authN)) {
            log.error("authentication object is of unsupported type: type={}", authNGeneric.getClass().getName());
            return new AuthorityAuthorizationDecision(false, Set.of());
        }
        var authorities = authN.getGrantedAuthorities();
        var authorityNames = authN.getAuthorityNames();

        // * SA always has full access to everything under all tenants
        if (authorityNames.contains(SA)) {
            return new AuthorityAuthorizationDecision(true, authorities);
        }

        // * for others, access to other tenants is not allowed
        var callersTenant = requestUtil.getCallersTenantUuid();
        var requestedTenant = requestUtil.getTenantUuid();
        if (!callersTenant.equals(requestedTenant)) {
            return new AuthorityAuthorizationDecision(false, authorities);
        }

        // * admin can access everything under their tenant
        if (authorityNames.contains(ADMIN)) {
            return new AuthorityAuthorizationDecision(true, authorities);
        }

        // * for all other roles, check if they have any of the required authorities
        var allowed = !intersection(authorityNames, anyOfAuthoritiesRequired).isEmpty();
        return new AuthorityAuthorizationDecision(allowed, authorities);
    }
}
