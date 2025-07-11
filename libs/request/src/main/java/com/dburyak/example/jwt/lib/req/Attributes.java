package com.dburyak.example.jwt.lib.req;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Attributes {
    public static final String AUTH_TOKEN = "authToken";
    public static final String API_KEY = "apiKey";
    public static final String CALLERS_TENANT_UUID = "callersTenantUuid";
    public static final String TENANT_UUID = "tenantUuid";
    public static final String USER_UUID = "userUuid";
    public static final String DEVICE_ID = "deviceId";
    public static final String ROLES = "roles";
    public static final String IS_SERVICE_REQ = "isServiceReq";
}
