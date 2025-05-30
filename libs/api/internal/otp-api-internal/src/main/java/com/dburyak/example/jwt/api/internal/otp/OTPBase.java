package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.AuditedApiModel;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class OTPBase extends AuditedApiModel {

    @JsonView({READ.class})
    protected final String deviceId;

    /**
     * IMPORTANT: in a real application, this value should never be exposed outside the otp-service. We make it
     * accessible only because we won't be sending real emails with OTPs in this example app. This is a clumsy way
     * to substitute actual email delivery.
     */
    @JsonView({READ.class})
    protected final String code;

    @JsonView({READ.class})
    protected final Instant expiresAt;
}
