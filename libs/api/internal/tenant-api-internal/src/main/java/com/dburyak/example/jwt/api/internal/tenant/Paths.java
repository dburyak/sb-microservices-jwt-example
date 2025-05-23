package com.dburyak.example.jwt.api.internal.tenant;

import lombok.experimental.UtilityClass;

import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_NAME;
import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_UUID;

@UtilityClass
public class Paths {
    // path components
    public static final String TENANT_EXISTS = "/exists";

    // full paths
    public static final String PATH_TENANT_EXISTS_BY_UUID = PATH_TENANT_BY_UUID + TENANT_EXISTS;
    public static final String PATH_TENANT_EXISTS_BY_NAME = PATH_TENANT_BY_NAME + TENANT_EXISTS;
}
