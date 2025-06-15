package com.dburyak.example.jwt.api.auth;

import lombok.experimental.UtilityClass;

import static com.dburyak.example.jwt.api.common.Paths.PATH_USER_ANONYMOUS;

@UtilityClass
public class Paths {
    // path components
    public static final String AUTH_JWT = "/auth/jwt";
    public static final String AUTH_JWT_TOKEN = "/token";
    public static final String AUTH_JWT_REFRESH = "/refresh";
    public static final String USER_PASSWORD = "/password";
    public static final String USER_PASSWORD_RESET = "/reset";
    public static final String USER_PASSWORD_CHANGE = "/change";
    public static final String OTP = "/otp";

    // full paths
    public static final String PATH_AUTH_JWT_TOKEN = AUTH_JWT + AUTH_JWT_TOKEN;
    public static final String PATH_AUTH_JWT_REFRESH = AUTH_JWT + AUTH_JWT_REFRESH;
    public static final String PATH_USER_PASSWORD_RESET = PATH_USER_ANONYMOUS + USER_PASSWORD + USER_PASSWORD_RESET;
    public static final String PATH_USER_PASSWORD_RESET_OTP = PATH_USER_PASSWORD_RESET + OTP;
}
