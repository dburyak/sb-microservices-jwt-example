package com.dburyak.example.jwt.api.internal.auth;

import com.dburyak.example.jwt.api.internal.auth.cfg.AuthServiceClientProperties;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import com.dburyak.example.jwt.lib.req.Attributes;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static com.dburyak.example.jwt.api.internal.auth.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.lib.req.Headers.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class AuthServiceClient {
    private final RestClient rest;
    private final ServiceTokenManager tokenManager;

    public AuthServiceClient(AuthServiceClientProperties props, ServiceTokenManager tokenManager) {
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
        this.tokenManager = tokenManager;
    }

    public User createUser(UUID tenantUuid, User user) {
        return rest.post()
                .uri(u -> u
                        .path(USERS_ROOT)
                        .queryParam(Attributes.TENANT_UUID, tenantUuid)
                        .build())
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER.getHeader() + tokenManager.getServiceToken())
                .body(user)
                .retrieve()
                .body(User.class);
    }
}
