package com.dburyak.example.jwt.lib.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {
    private final JwtAuthoritiesMapper rolesMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuth jwtAuth) {
            var mappedAuthorities = rolesMapper.mapToAuthorities(jwtAuth.getRoles());
            return jwtAuth.withMappedAuthorities(mappedAuthorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuth.class.isAssignableFrom(authentication);
    }
}
