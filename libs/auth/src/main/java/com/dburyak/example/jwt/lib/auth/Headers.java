package com.dburyak.example.jwt.lib.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Headers {
    TENANT_ID("x-tenant-id");

    @Getter
    private final String header;
}
