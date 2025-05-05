package com.dburyak.example.jwt.api.internal.otp;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Paths {
    public static final String OTPS_ROOT = "/otps";
    public static final String OTPS_BY_TYPE = "/{" + PathParams.OTP_TYPE + "}";
    public static final String OTP_BY_CODE = "/{" + PathParams.OTP_CODE + "}";
}
