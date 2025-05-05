package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.AuditedApiModel;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class OTP extends AuditedApiModel {
    UUID userUuid;
    String deviceId;
    String externalId; // email, phone number, etc., when userUuid is not known (anonymous user)
    OTPType type;

    /**
     * IMPORTANT: in a real application, this value should never be exposed outside the otp-service. We make it
     * accessible only because we won't be sending real emails with OTPs in this example app. This is a clumsy way
     * to substitute actual email delivery.
     */
    String code;

    Instant expiresAt;
}
