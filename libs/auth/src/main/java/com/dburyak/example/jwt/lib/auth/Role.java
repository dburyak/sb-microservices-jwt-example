package com.dburyak.example.jwt.lib.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum Role {
    SUPER_ADMIN("sa"),
    ADMIN("adm"),
    CONTENT_MANAGER("cmg"),
    USER_MANAGER("umg"),
    USER("usr"),
    SERVICE("srv"); // special role for services, not exposed to users

    @Getter
    private final String name;

    private static final Map<String, Role> BY_NAME = Arrays.stream(Role.values())
            .collect(toMap(Role::getName, r -> r));

    public static Role byName(String roleName) {
        return BY_NAME.get(roleName);
    }
}
