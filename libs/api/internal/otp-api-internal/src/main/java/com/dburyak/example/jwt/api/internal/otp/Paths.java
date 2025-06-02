package com.dburyak.example.jwt.api.internal.otp;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    // path components
    public static final String OTPS = "/otps";
    public static final String OTP_BY_TYPE = "/{" + PathParams.OTP_TYPE + "}";
    public static final String OTP_BY_CODE = "/{" + PathParams.OTP_CODE + "}";
    public static final String ANONYMOUS = "/anonymous";

    // full paths
    public static final String PATH_REGISTERED_OTPS = OTPS;
    public static final String PATH_REGISTERED_OTP_BY_TYPE = PATH_REGISTERED_OTPS + OTP_BY_TYPE;
    public static final String PATH_REGISTERED_OTP_BY_CODE = PATH_REGISTERED_OTP_BY_TYPE + OTP_BY_CODE;
    public static final String PATH_ANONYMOUS_OTPS = OTPS + ANONYMOUS;
    public static final String PATH_ANONYMOUS_OTP_BY_TYPE = PATH_ANONYMOUS_OTPS + OTP_BY_TYPE;
    public static final String PATH_ANONYMOUS_OTP_BY_CODE = PATH_ANONYMOUS_OTP_BY_TYPE + OTP_BY_CODE;
}
