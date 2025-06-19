package com.dburyak.example.jwt.api.internal.auth;

import com.dburyak.example.jwt.api.auth.User;
import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.auth.cfg.AuthServiceClientProperties;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.lib.req.Headers.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class AuthServiceClientInternal {
    private final RestClient rest;
    private final RequestUtil requestUtil;
    private final ServiceTokenManager tokenManager;

    public AuthServiceClientInternal(AuthServiceClientProperties props, RequestUtil requestUtil,
            ServiceTokenManager tokenManager) {
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
        this.requestUtil = requestUtil;
        this.tokenManager = tokenManager;
    }

    public User createUser(UUID tenantUuid, User user) {
        return requestUtil.withPropagatedAuthOrToken(tokenManager::getServiceToken, rest.post()
                        .uri(u -> u
                                .path(USERS)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                .build())
                        .contentType(APPLICATION_JSON)
                )
                .header(AUTHORIZATION, BEARER + tokenManager.getServiceToken())
                .body(user)
                .retrieve()
                .body(User.class);
    }
}
