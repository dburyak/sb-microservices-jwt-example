package com.dburyak.example.jwt.auth.cfg;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.Role;
import com.dburyak.example.jwt.lib.auth.StandardAuthorities;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_ALL_READ;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_ALL_WRITE;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_READ;
import static com.dburyak.example.jwt.auth.cfg.Authorities.USER_WRITE;
import static com.dburyak.example.jwt.lib.auth.Role.ADMIN;
import static com.dburyak.example.jwt.lib.auth.Role.SERVICE;
import static com.dburyak.example.jwt.lib.auth.Role.SUPER_ADMIN;
import static com.dburyak.example.jwt.lib.auth.Role.USER_MANAGER;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.SA;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Component
public class AuthRolesMapper implements AuthoritiesMapper {
    private final Map<Role, Set<String>> mapping = Map.of(
            SUPER_ADMIN, Set.of(SA, USER_ALL_WRITE, USER_ALL_READ),
            ADMIN, Set.of(StandardAuthorities.ADMIN, USER_WRITE, USER_READ),
            USER_MANAGER, Set.of(USER_WRITE, USER_READ),
            SERVICE, Set.of(USER_ALL_WRITE, USER_ALL_READ)
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
