package com.dburyak.example.jwt.api.user;

import com.dburyak.example.jwt.api.common.PathParams;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    public static final String USERS_ROOT = "/users";
    public static final String USER_BY_UUID = "/{" + PathParams.USER_UUID + "}";
    public static final String CONTACT_INFO = "/contact-info";
}
