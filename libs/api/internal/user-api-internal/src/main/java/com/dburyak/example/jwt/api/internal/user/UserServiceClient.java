package com.dburyak.example.jwt.api.internal.user;

import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.user.cfg.UserServiceClientProperties;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static com.dburyak.example.jwt.api.common.Paths.PATH_USER_BY_UUID;
import static com.dburyak.example.jwt.api.user.Paths.CONTACT_INFO;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class UserServiceClient {
    private final RequestUtil requestUtil;
    private final RestClient rest;

    public UserServiceClient(RequestUtil requestUtil, UserServiceClientProperties props) {
        this.requestUtil = requestUtil;
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
    }

    public ContactInfo getContactInfo(UUID userUuid) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_USER_BY_UUID + CONTACT_INFO)
                                .build(userUuid))
                        .accept(APPLICATION_JSON)
                )
                .retrieve()
                .body(ContactInfo.class);
    }

    public ContactInfo getContactInfo(UUID tenantUuid, UUID userUuid) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_USER_BY_UUID + CONTACT_INFO)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid) // in case if it's a service request
                                .build(userUuid))
                        .accept(APPLICATION_JSON)
                )
                .retrieve()
                .body(ContactInfo.class);
    }
}
