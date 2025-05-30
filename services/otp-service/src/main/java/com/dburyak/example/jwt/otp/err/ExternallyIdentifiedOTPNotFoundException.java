package com.dburyak.example.jwt.otp.err;

import com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.lib.err.NotFoundException;

public class ExternallyIdentifiedOTPNotFoundException extends NotFoundException {

    public ExternallyIdentifiedOTPNotFoundException(String deviceId, ExternallyIdentifiedOTP.Type type) {
        super(resourceName(deviceId, type));
    }

    public ExternallyIdentifiedOTPNotFoundException(String deviceId, ExternallyIdentifiedOTP.Type type, String code) {
        super(resourceName(deviceId, type, code));
    }

    private static String resourceName(String deviceId, ExternallyIdentifiedOTP.Type type) {
        return "OTP(deviceId=%s, type=%s)".formatted(deviceId, type);
    }

    private static String resourceName(String deviceId, ExternallyIdentifiedOTP.Type type, String code) {
        return "OTP(deviceId=%s, type=%s, code=%s)".formatted(deviceId, type, code);
    }
}
