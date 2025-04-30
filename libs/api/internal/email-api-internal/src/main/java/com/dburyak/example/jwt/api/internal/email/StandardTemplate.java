package com.dburyak.example.jwt.api.internal.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StandardTemplate {
    PASSWORD_RESET("password-reset");

    private final String templateName;
}
