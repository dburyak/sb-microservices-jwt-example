package com.dburyak.example.jwt.lib.auth.jwt;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.Role;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * A no-op implementation of {@link AuthoritiesMapper} that returns an empty set of authorities. Only useful as a
 * stub.
 */
public class NoAuthoritiesMapper implements AuthoritiesMapper {

    @Override
    public Set<String> mapToAuthorities(Set<Role> jwtRoles) {
        return emptySet();
    }
}
