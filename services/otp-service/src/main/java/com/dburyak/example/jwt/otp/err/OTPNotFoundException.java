package com.dburyak.example.jwt.otp.err;

import com.dburyak.example.jwt.api.internal.otp.OTPType;
import com.dburyak.example.jwt.lib.err.NotFoundException;

import java.util.UUID;

public class OTPNotFoundException extends NotFoundException {

    public OTPNotFoundException(UUID userUuid, String deviceId, OTPType type) {
        super(resourceName(userUuid, deviceId, type));
    }

    public OTPNotFoundException(UUID userUuid, String deviceId, OTPType type, String code) {
        super(resourceName(userUuid, deviceId, type, code));
    }

    private static String resourceName(UUID userUuid, String deviceId, OTPType type) {
        return "OTP(userUuid=%s, deviceId=%s, type=%s)".formatted(userUuid, deviceId, type);
    }

    private static String resourceName(UUID userUuid, String deviceId, OTPType type, String code) {
        return "OTP(userUuid=%s, deviceId=%s, type=%s, code=%s)".formatted(userUuid, deviceId, type, code);
    }
}
