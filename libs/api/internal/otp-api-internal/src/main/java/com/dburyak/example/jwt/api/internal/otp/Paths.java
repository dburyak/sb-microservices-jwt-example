package com.dburyak.example.jwt.api.internal.otp;

import lombok.experimental.UtilityClass;

import static com.dburyak.example.jwt.api.common.Paths.PATH_DEVICE_BY_ID;

@UtilityClass
public class Paths {
    // path components
    public static final String OTPS = "/otps";
    public static final String OTP_BY_TYPE = "/{" + PathParams.OTP_TYPE + "}";
    public static final String OTP_BY_CODE = "/{" + PathParams.OTP_CODE + "}";

    // full paths
    public static final String PATH_OTPS = PATH_DEVICE_BY_ID + OTPS;
    public static final String PATH_OTP_BY_TYPE = PATH_OTPS + OTP_BY_TYPE;
    public static final String PATH_OTP_BY_CODE = PATH_OTP_BY_TYPE + OTP_BY_CODE;
}
