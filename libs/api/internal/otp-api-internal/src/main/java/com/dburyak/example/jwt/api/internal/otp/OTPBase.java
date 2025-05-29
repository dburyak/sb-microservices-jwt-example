package com.dburyak.example.jwt.api.internal.otp;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;

@Data
@RequiredArgsConstructor(access = PROTECTED)
public abstract class OTPBase {
    private final String deviceId;
    private final OTPType type;

    /**
     * IMPORTANT: in a real application, this value should never be exposed outside the otp-service. We make it
     * accessible only because we won't be sending real emails with OTPs in this example app. This is a clumsy way
     * to substitute actual email delivery.
     */
    private final String code;

    private final Instant expiresAt;
}
