package com.dburyak.example.jwt.lib.req;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.UUID;

@UtilityClass
public class ReservedIdentifiers {
    // reserved user uuids
    public static final UUID SA_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final UUID AUTH_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public static final UUID USER_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    public static final UUID TENANT_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    public static final UUID OTP_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000004");
    public static final UUID EMAIL_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000005");
    public static final Map<String, UUID> SERVICE_UUIDS = Map.of(
            "auth-service", AUTH_SERVICE_USER_UUID,
            "user-service", USER_SERVICE_USER_UUID,
            "tenant-service", TENANT_SERVICE_USER_UUID,
            "otp-service", OTP_SERVICE_USER_UUID,
            "email-service", EMAIL_SERVICE_USER_UUID
    );

    // reserved tenant uuids
    public static final UUID SA_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final UUID SERVICE_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    // reserved device ids
    public static final String SERVICE_DEVICE_ID = "srv";
}
