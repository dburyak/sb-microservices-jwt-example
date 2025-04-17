package com.dburyak.example.jwt.lib.auth.jwt;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {
    private final AuthoritiesMapper rolesMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuth jwtAuth) {
            var mappedAuthorities = rolesMapper.mapToAuthorities(jwtAuth.getRoles());
            return jwtAuth.withMappedAuthorities(mappedAuthorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return JwtAuth.class.isAssignableFrom(authenticationType);
    }
}
