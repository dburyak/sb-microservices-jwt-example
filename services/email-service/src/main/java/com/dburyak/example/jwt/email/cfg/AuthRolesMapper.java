package com.dburyak.example.jwt.email.cfg;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.Role;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.dburyak.example.jwt.email.cfg.Authorities.SEND_EMAIL;

@Component
public class AuthRolesMapper implements AuthoritiesMapper {
    private final Set<String> sendEmailAuthorities = Set.of(SEND_EMAIL);

    @Override
    public Set<String> mapToAuthorities(Set<Role> jwtRoles) {
        // Any authenticated user can send emails. In a more complex case, we'd split authorities into more
        // fine-grained cases that depend on the type of email - password reset, advertisement, etc.
        return sendEmailAuthorities;
    }
}
