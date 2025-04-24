package com.dburyak.example.jwt.api.auth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    public static final String AUTH_JWT_ROOT = "/auth/jwt";
    public static final String AUTH_JWT_TOKEN = "/token";
    public static final String AUTH_JWT_REFRESH = "/refresh";

    public static final String USER_ROOT = "/users";
    public static final String USER_PASSWORD = "/password";
    public static final String USER_PASSWORD_RESET_OTP = "/reset-otp";
}
