package com.dburyak.example.jwt.lib.auth.apikey;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class ApiKeyAuthProvider implements AuthenticationProvider {
    private final AuthoritiesMapper rolesMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof ApiKeyAuth apiKeyAuth) {
            // TODO: make a call to the auth service to retrieve the roles for the api-key+tenantUuid
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuth.class.isAssignableFrom(authentication);
    }
}
