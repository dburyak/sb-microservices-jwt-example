package com.dburyak.example.jwt.api.tenant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    // path components
    public static final String TENANTS = "/tenants";
    public static final String TENANT_BY_UUID = "/{" + PathParams.TENANT_UUID + "}";
    public static final String BY_NAME = "/by-name";
    public static final String TENANT_BY_NAME = "/{" + PathParams.TENANT_NAME + "}";

    // full paths
    public static final String PATH_TENANT_BY_UUID = TENANTS + TENANT_BY_UUID;
    public static final String PATH_TENANT_BY_NAME = TENANTS + BY_NAME + TENANT_BY_NAME;
}
