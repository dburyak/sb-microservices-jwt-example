package com.dburyak.example.jwt.api.internal.user;

import com.dburyak.example.jwt.api.internal.user.cfg.UserServiceClientProperties;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.web.client.RestClient;

public class UserServiceClientInternal {
    private final RequestUtil requestUtil;
    private final RestClient rest;

    public UserServiceClientInternal(RequestUtil requestUtil, UserServiceClientProperties props) {
        this.requestUtil = requestUtil;
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
    }
}
