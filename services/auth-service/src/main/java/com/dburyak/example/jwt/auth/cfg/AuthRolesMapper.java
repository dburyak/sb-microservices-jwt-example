package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.JwtAuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.Role;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Component
public class AuthRolesMapper implements JwtAuthoritiesMapper {
    private final Map<Role, Set<String>> mapping = Map.of(
            Role.SERVICE, Set.of(Authorities.USER_CREATE.name())
    );

    @Override
    public Set<String> mapToAuthorities(Set<Role> jwtRoles) {
        if (jwtRoles.size() <= 1) {
            // small optimization to avoid extra copy of Set
            return jwtRoles.size() == 1 ? mapping.get(jwtRoles.iterator().next()) : emptySet();
        } else {
            return jwtRoles.stream()
                    .flatMap(role -> mapping.getOrDefault(role, emptySet()).stream())
                    .collect(toSet());
        }
    }
}
