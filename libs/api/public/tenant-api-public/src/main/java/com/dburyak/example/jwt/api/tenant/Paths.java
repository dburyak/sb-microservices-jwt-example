package com.dburyak.example.jwt.api.tenant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    public static final String TENANTS_ROOT = "/tenants";
    public static final String TENANT_BY_UUID = "/{" + PathParams.TENANT_UUID + "}";
    public static final String TENANT_BY_NAME = "by-name/{" + PathParams.TENANT_NAME + "}";
}
