package com.dburyak.example.jwt.lib.req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Headers {
    BEARER("Bearer "), // technically it's not a header, but it's related
    TENANT_ID("x-tenant-id");

    @Getter
    private final String header;
}
