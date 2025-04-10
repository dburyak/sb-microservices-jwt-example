package com.dburyak.example.jwt.lib.auth;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Getter
public class JwtAuth implements Authentication {
    private final UUID tenantUuid;
    private final UUID userUuid;
    private final String deviceId;
    private final Set<Role> roles;
    private final Set<GrantedAuthority> authorities;
    private final boolean authenticated;

    public JwtAuth(UUID tenantUuid, UUID userUuid, String deviceId, Set<Role> roles) {
        this.tenantUuid = tenantUuid;
        this.userUuid = userUuid;
        this.deviceId = deviceId;
        this.roles = roles;
        authorities = null;
        authenticated = false;
    }

    private JwtAuth(UUID tenantUuid, UUID userUuid, String deviceId, Set<Role> roles, Set<String> authorities) {
        this.tenantUuid = tenantUuid;
        this.userUuid = userUuid;
        this.deviceId = deviceId;
        this.roles = roles;
        this.authorities = (authorities != null)
                ? Set.copyOf(authorities.stream().map(SimpleGrantedAuthority::new).collect(toSet()))
                : null;
        authenticated = authorities != null;
    }

    public JwtAuth withMappedAuthorities(Set<String> authorities) {
        return new JwtAuth(tenantUuid, userUuid, deviceId, roles, authorities);
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
        return userUuid;
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
        return userUuid.toString();
    }
}
