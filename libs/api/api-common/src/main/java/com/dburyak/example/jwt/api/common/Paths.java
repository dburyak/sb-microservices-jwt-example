package com.dburyak.example.jwt.api.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    // path components
    public static final String USERS = "/users";
    public static final String DEVICES = "/devices";
    public static final String USER_BY_UUID = "/{" + PathParams.USER_UUID + "}";
    public static final String DEVICE_BY_ID = "/{" + PathParams.DEVICE_ID + "}";

    // full paths
    public static final String PATH_USER_BY_UUID = USERS + USER_BY_UUID;
    public static final String PATH_DEVICE_BY_ID = PATH_USER_BY_UUID + DEVICES + DEVICE_BY_ID;
}
