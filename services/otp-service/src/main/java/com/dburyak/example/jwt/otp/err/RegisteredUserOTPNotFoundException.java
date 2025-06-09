package com.dburyak.example.jwt.otp.err;

import com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP;
import com.dburyak.example.jwt.lib.err.NotFoundException;

import java.util.UUID;

public class RegisteredUserOTPNotFoundException extends NotFoundException {

    public RegisteredUserOTPNotFoundException(UUID userUuid, String deviceId, RegisteredUserOTP.Type type) {
        super(resourceName(userUuid, deviceId, type));
    }

    public RegisteredUserOTPNotFoundException(UUID userUuid, String deviceId, RegisteredUserOTP.Type type, String code) {
        super(resourceName(userUuid, deviceId, type, code));
    }

    private static String resourceName(UUID userUuid, String deviceId, RegisteredUserOTP.Type type) {
        return "OTP(userUuid=%s, deviceId=%s, type=%s)".formatted(userUuid, deviceId, type);
    }

    private static String resourceName(UUID userUuid, String deviceId, RegisteredUserOTP.Type type, String code) {
        return "OTP(userUuid=%s, deviceId=%s, type=%s, code=%s)".formatted(userUuid, deviceId, type, code);
    }
}
