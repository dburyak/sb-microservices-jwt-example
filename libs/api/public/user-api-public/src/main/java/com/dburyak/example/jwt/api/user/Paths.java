package com.dburyak.example.jwt.api.user;

import lombok.experimental.UtilityClass;

import static com.dburyak.example.jwt.api.common.Paths.PATH_USER_BY_UUID;

@UtilityClass
public class Paths {
    // path components
    public static final String CONTACT_INFO = "/contact-info";
    public static final String REGISTRATION_OTP = "/registration-otp";

    // full paths
    public static final String PATH_CONTACT_INFO = PATH_USER_BY_UUID + CONTACT_INFO;
}
