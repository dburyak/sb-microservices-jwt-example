package com.dburyak.example.jwt.lib.auth.jwt;

import com.dburyak.example.jwt.lib.auth.AbstractAppAuthentication;
import com.dburyak.example.jwt.lib.auth.Role;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public class JwtAuth extends AbstractAppAuthentication {
    private final String jwtToken;

    public JwtAuth(String jwtToken, UUID tenantUuid, UUID userUuid, String deviceId, Set<Role> roles) {
        this(jwtToken, tenantUuid, userUuid, deviceId, roles, null);
    }

    private JwtAuth(String jwtToken, UUID tenantUuid, UUID userUuid, String deviceId, Set<Role> roles,
            Set<String> authorities) {
        super(tenantUuid, userUuid, deviceId, roles, authorities);
        this.jwtToken = jwtToken;
    }

    public JwtAuth withMappedAuthorities(Set<String> authorities) {
        return new JwtAuth(jwtToken, tenantUuid, userUuid, deviceId, roles, authorities);
    }
}
