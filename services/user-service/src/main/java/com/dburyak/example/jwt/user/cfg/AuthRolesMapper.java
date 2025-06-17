package com.dburyak.example.jwt.user.cfg;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.Role;
import com.dburyak.example.jwt.lib.auth.StandardAuthorities;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.dburyak.example.jwt.lib.auth.Role.SERVICE;
import static com.dburyak.example.jwt.lib.auth.Role.SUPER_ADMIN;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.SA;
import static com.dburyak.example.jwt.user.cfg.Authorities.USER_MANAGE;
import static java.util.stream.Collectors.toSet;

@Component
public class AuthRolesMapper implements AuthoritiesMapper {
    private final Map<Role, Set<String>> mapping = Map.of(
            SUPER_ADMIN, Set.of(SA),
            Role.ADMIN, Set.of(StandardAuthorities.ADMIN),
            SERVICE, Set.of(USER_MANAGE)
    );

    @Override
    public Set<String> mapToAuthorities(Set<Role> jwtRoles) {
        if (jwtRoles.size() <= 1) {
            // small optimization to avoid extra copy of Set
            return jwtRoles.size() == 1 ? mapping.get(jwtRoles.iterator().next()) : Set.of();
        } else {
            return jwtRoles.stream()
                    .flatMap(role -> mapping.getOrDefault(role, Set.of()).stream())
                    .collect(toSet());
        }
    }
}
