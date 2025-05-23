package com.dburyak.example.jwt.api.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    // path components
    public static final String USERS = "/users";
    public static final String DEVICES = "/devices";
    public static final String ANONYMOUS = "/anonymous";
    public static final String USER_BY_UUID = "/{" + PathParams.USER_UUID + "}";
    public static final String DEVICE_BY_ID = "/{" + PathParams.DEVICE_ID + "}";

    // "get" and "delete" paths for cases when we have to pass sensitive data (emails) and thus can't use
    // path or query parameters and have to use request body with POST instead
    public static final String GET_RESOURCE = "/get";
    public static final String DELETE_RESOURCE = "/delete";

    // full paths
    public static final String PATH_USER_BY_UUID = USERS + USER_BY_UUID;
    public static final String PATH_DEVICE_BY_ID = PATH_USER_BY_UUID + DEVICES + DEVICE_BY_ID;
    public static final String PATH_ANONYMOUS_DEVICE_BY_ID =
            USERS + ANONYMOUS + DEVICES + DEVICE_BY_ID;
}
