package com.dburyak.example.jwt.lib.auth.apikey;

import com.dburyak.example.jwt.lib.auth.AbstractAppAuthentication;
import com.dburyak.example.jwt.lib.auth.Role;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public class ApiKeyAuth extends AbstractAppAuthentication {
    private final String apiKey;

    public ApiKeyAuth(String apiKey) {
        super(null, null, null, null, null);
        this.apiKey = apiKey;
    }

    private ApiKeyAuth(String apiKey, UUID tenantUuid, UUID userUuid, String deviceId, Set<Role> roles,
            Set<String> authorities) {
        super(tenantUuid, userUuid, deviceId, roles, authorities);
        this.apiKey = apiKey;
    }

    public ApiKeyAuth withDetails(UUID tenantUuid, UUID userUuid, String deviceId, Set<Role> roles,
            Set<String> authorities) {
        return new ApiKeyAuth(apiKey, tenantUuid, userUuid, deviceId, roles, authorities);
    }
}
