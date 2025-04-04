package com.dburyak.example.jwt.lib.auth;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AuthConstants {
    public static final String SERVICE_TENANT_ID = "service";
    public static final String SERVICE_DEVICE_ID = "service";
    public static final UUID AUTH_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final UUID USER_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
}
