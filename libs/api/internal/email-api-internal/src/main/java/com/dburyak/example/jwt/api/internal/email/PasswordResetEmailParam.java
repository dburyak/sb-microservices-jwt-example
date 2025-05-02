package com.dburyak.example.jwt.api.internal.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PasswordResetEmailParam {
    OTP("otp"),
    LINK("link");

    private final String paramName;
}
