package com.dburyak.example.jwt.api.internal.auth;

import com.dburyak.example.jwt.lib.auth.apikey.ApiKeyAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * It should be defined here instead of {@code :libs:auth} to avoid circular dependency. This auth provider needs to use
 * a lot of components of {@code auth-api-internal} - paths, config properties, etc.
 */
@RequiredArgsConstructor
public class ApiKeyAuthProvider implements AuthenticationProvider {
    private final AuthServiceClientInternal authServiceClientInternal;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return ApiKeyAuth.class.isAssignableFrom(authenticationType);
    }
}
