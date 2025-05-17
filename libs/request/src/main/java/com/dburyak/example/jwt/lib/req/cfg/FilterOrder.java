package com.dburyak.example.jwt.lib.req.cfg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FilterOrder {
    EXTRACT_TENANT(0),
    JWT_AUTH(10),
    APIKEY_AUTH(20),
    VERIFY_TENANT(100);

    private final int order;
}
