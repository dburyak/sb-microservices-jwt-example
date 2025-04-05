package com.dburyak.example.jwt.lib.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    SUPER_ADMIN("sa"),
    ADMIN("adm"),
    CONTENT_MANAGER("cm"),
    USER_MANAGER("umg"),
    USER("usr"),
    SERVICE("srv"); // special role for services, not exposed to users

    @Getter
    private final String name;
}
