package com.dburyak.example.jwt.api.internal.auth;

import com.dburyak.example.jwt.api.internal.auth.cfg.AuthServiceClientProperties;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import org.springframework.web.client.RestClient;

import static com.dburyak.example.jwt.api.internal.auth.Paths.USER;
import static com.dburyak.example.jwt.lib.auth.Headers.TENANT_ID;
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

    public User createUser(String tenantId, User user) {
        return rest.post()
                .uri(USER)
                .contentType(APPLICATION_JSON)
                .header(TENANT_ID.getHeader(), tenantId)
                .header(AUTHORIZATION, "Bearer " + tokenManager.getServiceToken())
                .body(user)
                .retrieve()
                .body(User.class);
    }
}
