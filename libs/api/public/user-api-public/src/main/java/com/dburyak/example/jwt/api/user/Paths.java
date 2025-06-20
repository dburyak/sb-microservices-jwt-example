package com.dburyak.example.jwt.api.user;

import lombok.experimental.UtilityClass;

import static com.dburyak.example.jwt.api.common.Paths.PATH_USER_BY_UUID;
import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.api.common.Paths.USER_BY_UUID;

@UtilityClass
public class Paths {
    // path components
    public static final String CONTACT_INFO = "/contact-info";
    public static final String REGISTRATION_OTP = "/registration-otp";
    public static final String MANAGEMENT = "/management";
    public static final String BY_USERNAME = "/by-username";
    public static final String USER_BY_USERNAME = "/{" + PathParams.USERNAME + "}";

    // full paths
    public static final String PATH_USER_REGISTRATION_OTP = USERS + REGISTRATION_OTP;
    public static final String PATH_CONTACT_INFO = PATH_USER_BY_UUID + CONTACT_INFO;
    public static final String PATH_USER_MANAGEMENT = USERS + MANAGEMENT;
    public static final String PATH_USER_MANAGEMENT_BY_USERNAME = PATH_USER_MANAGEMENT + BY_USERNAME + USER_BY_USERNAME;
    public static final String PATH_USER_MANAGEMENT_BY_UUID = PATH_USER_MANAGEMENT + USER_BY_UUID;
}
