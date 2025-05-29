package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.AuditedApiModel;
import com.dburyak.example.jwt.api.common.ExternalId;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class ExternallyIdentifiedOTP extends AuditedApiModel {
    ExternalId externalId;
    String deviceId;
    Type type;

    /**
     * IMPORTANT: in a real application, this value should never be exposed outside the otp-service. We make it
     * accessible only because we won't be sending real emails with OTPs in this example app. This is a clumsy way
     * to substitute actual email delivery.
     */
    String code;

    Instant expiresAt;

    public enum Type {
        EMAIL_REGISTRATION,
        PASSWORD_RESET,
    }

}
