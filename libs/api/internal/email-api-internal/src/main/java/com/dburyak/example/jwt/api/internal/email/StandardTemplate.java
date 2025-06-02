package com.dburyak.example.jwt.api.internal.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StandardTemplate {
    PASSWORD_RESET("password-reset"),
    REGISTRATION_WITH_EMAIL("registration-with-email"),
    CHANGE_PASSWORD("change-password");

    private final String templateName;
}
