package com.dburyak.example.jwt.otp.cfg;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.Role;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.dburyak.example.jwt.lib.auth.Role.ADMIN;
import static com.dburyak.example.jwt.lib.auth.Role.CONTENT_MANAGER;
import static com.dburyak.example.jwt.lib.auth.Role.SERVICE;
import static com.dburyak.example.jwt.lib.auth.Role.SUPER_ADMIN;
import static com.dburyak.example.jwt.lib.auth.Role.USER;
import static com.dburyak.example.jwt.lib.auth.Role.USER_MANAGER;
import static com.dburyak.example.jwt.otp.cfg.Authorities.OTP_CLAIM_OWN;
import static com.dburyak.example.jwt.otp.cfg.Authorities.OTP_CREATE_OWN;
import static com.dburyak.example.jwt.otp.cfg.Authorities.OTP_PASSWORD_RESET_CREATE_ALL;
import static com.dburyak.example.jwt.otp.cfg.Authorities.OTP_PASSWORD_RESET_DELETE_ALL;
import static com.dburyak.example.jwt.otp.cfg.Authorities.SA;
import static com.google.common.collect.Sets.union;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Component
public class AuthRolesMapper implements AuthoritiesMapper {
    private static final Set<String> CREATE_AND_CLAIM_OWN = Set.of(
            OTP_CREATE_OWN,
            OTP_CLAIM_OWN
    );
    private final Map<Role, Set<String>> mapping = Map.of(
            SUPER_ADMIN, union(CREATE_AND_CLAIM_OWN, Set.of(
                    SA,
                    OTP_PASSWORD_RESET_CREATE_ALL,
                    OTP_PASSWORD_RESET_DELETE_ALL)),
            ADMIN, union(CREATE_AND_CLAIM_OWN, Set.of(
                    OTP_PASSWORD_RESET_CREATE_ALL,
                    OTP_PASSWORD_RESET_DELETE_ALL
            )),
            CONTENT_MANAGER, CREATE_AND_CLAIM_OWN,
            USER_MANAGER, union(CREATE_AND_CLAIM_OWN, Set.of(
                    OTP_PASSWORD_RESET_CREATE_ALL,
                    OTP_PASSWORD_RESET_DELETE_ALL
            )),
            USER, CREATE_AND_CLAIM_OWN,
            SERVICE, Set.of(
                    OTP_PASSWORD_RESET_CREATE_ALL,
                    OTP_PASSWORD_RESET_DELETE_ALL
            )
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
