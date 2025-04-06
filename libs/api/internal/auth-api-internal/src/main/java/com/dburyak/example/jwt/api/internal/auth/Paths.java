package com.dburyak.example.jwt.api.internal.auth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    public static final String AUTH_JWT = "/auth/jwt";
    public static final String AUTH_JWT_TOKEN = AUTH_JWT + "/token";
    public static final String AUTH_JWT_REFRESH = AUTH_JWT + "/refresh";
    public static final String USER = "/user";
}
