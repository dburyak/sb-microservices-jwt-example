package com.dburyak.example.jwt.otp.cfg;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Authorities {
    public static final String SA = "SA";
    public static final String OTP_CREATE_OWN = "OTP_CREATE_OWN";
    public static final String OTP_CLAIM_OWN = "OTP_CLAIM_OWN";
    public static final String OTP_PASSWORD_RESET_CREATE_ALL = "OTP_PASSWORD_RESET_CREATE_ALL";
    public static final String OTP_PASSWORD_RESET_DELETE_ALL = "OTP_PASSWORD_RESET_CLAIM_ALL";
}
