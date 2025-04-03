package com.dburyak.example.jwt.lib.auth;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Getter
public class JwtAuth implements Authentication {
    private final String tenantId;
    private final String userId;
    private final Set<String> roles;
    private final Set<GrantedAuthority> authorities;
    private final boolean authenticated;

    public JwtAuth(String tenantId, String userId, Set<String> roles) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.roles = roles;
        authorities = null;
        authenticated = false;
    }

    private JwtAuth(String tenantId, String username, Set<String> roles, Set<String> authorities) {
        this.tenantId = tenantId;
        this.userId = username;
        this.roles = roles;
        this.authorities = (authorities != null)
                ? Set.copyOf(authorities.stream().map(SimpleGrantedAuthority::new).collect(toSet()))
                : null;
        authenticated = authorities != null;
    }

    public JwtAuth withMappedAuthorities(Set<String> authorities) {
        return new JwtAuth(tenantId, userId, roles, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities != null ? authorities : emptySet();
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException("is not supposed to be called");
    }

    @Override
    public Object getDetails() {
        throw new UnsupportedOperationException("is not supposed to be called");
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("is not supposed to be called");
    }

    @Override
    public String getName() {
        return userId;
    }
}
