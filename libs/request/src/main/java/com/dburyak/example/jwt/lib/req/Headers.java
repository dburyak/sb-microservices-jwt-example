package com.dburyak.example.jwt.lib.req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Headers {
    BEARER("Bearer "), // technically it's not a header, but it's related to headers processing
    TENANT_UUID("x-tenant-uuid");

    @Getter
    private final String header;
}
