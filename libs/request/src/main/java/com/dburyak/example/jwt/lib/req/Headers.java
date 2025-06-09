package com.dburyak.example.jwt.lib.req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
public enum Headers {
    AUTHORIZATION(HttpHeaders.AUTHORIZATION),
    TENANT_UUID("x-tenant-uuid"),
    API_KEY("x-api-key");

    // Technically it's not a header, but it's related to headers processing.
    public static final String BEARER = "Bearer ";

    @Getter
    private final String header;
}
