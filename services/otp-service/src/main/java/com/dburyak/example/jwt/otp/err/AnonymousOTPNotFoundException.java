package com.dburyak.example.jwt.otp.err;

import com.dburyak.example.jwt.api.internal.otp.OTPType;
import com.dburyak.example.jwt.lib.err.NotFoundException;

public class AnonymousOTPNotFoundException extends NotFoundException {

    public AnonymousOTPNotFoundException(String deviceId, OTPType type) {
        super(resourceName(deviceId, type));
    }

    public AnonymousOTPNotFoundException(String deviceId, OTPType type, String code) {
        super(resourceName(deviceId, type, code));
    }

    private static String resourceName(String deviceId, OTPType type) {
        return "OTP(deviceId=%s, type=%s)".formatted(deviceId, type);
    }

    private static String resourceName(String deviceId, OTPType type, String code) {
        return "OTP(deviceId=%s, type=%s, code=%s)".formatted(deviceId, type, code);
    }
}
