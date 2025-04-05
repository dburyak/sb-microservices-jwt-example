package com.dburyak.example.jwt.lib.auth;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * A no-op implementation of {@link JwtAuthoritiesMapper} that returns an empty set of authorities. Only useful as a
 * stub.
 */
public class NoAuthoritiesMapper implements JwtAuthoritiesMapper {

    @Override
    public Set<String> mapToAuthorities(Set<Role> jwtRoles) {
        return emptySet();
    }
}
