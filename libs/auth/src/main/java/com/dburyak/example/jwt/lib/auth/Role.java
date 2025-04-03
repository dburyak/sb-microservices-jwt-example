package com.dburyak.example.jwt.lib.auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    SUPER_ADMIN("sa"),
    ADMIN("adm"),
    CONTENT_MANAGER("cm"),
    USER_MANAGER("umg"),
    USER("usr");

    private final String name;
}
