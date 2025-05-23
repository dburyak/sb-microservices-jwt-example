package com.dburyak.example.jwt.api.auth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    // path components
    public static final String AUTH_JWT = "/auth/jwt";
    public static final String AUTH_JWT_TOKEN = "/token";
    public static final String AUTH_JWT_REFRESH = "/refresh";
    public static final String USER_PASSWORD = "/password";
    public static final String USER_PASSWORD_RESET_OTP = "/reset-otp";

    // full paths
    public static final String PATH_AUTH_JWT_TOKEN = AUTH_JWT + AUTH_JWT_TOKEN;
    public static final String PATH_AUTH_JWT_REFRESH = AUTH_JWT + AUTH_JWT_REFRESH;
}
