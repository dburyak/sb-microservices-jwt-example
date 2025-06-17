package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthZFactory {
    private final RequestUtil requestUtil;

    public AuthZAnyOf anyOf(String... authorities) {
        return new AuthZAnyOf(requestUtil, authorities);
    }

    public AuthZAnyOf authority(String authority) {
        return new AuthZAnyOf(requestUtil, authority);
    }
}
