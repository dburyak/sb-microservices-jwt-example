package com.dburyak.example.jwt.api.internal.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Create email OTP request message originating from an anonymous user (not registered or not authenticated). In this
 * case, it is identified by the email if userUuid is not known. But in more general case, it may be any other external
 * identifier (phone number, social network account, etc.).
 * Example scenarios: registration, password reset.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class CreateEmailOTPForAnonymousUserMsg extends CreateOTPBaseMsg {

    @NotNull
    UUID tenantUuid;

    /**
     * Optional, used for cases when the caller is registered but not authenticated (no auth token with all the info).
     * Otherwise, the userUuid is not known and external identifier should be used instead (email, phone number, etc.).
     */
    UUID userUuid;

    @NotBlank
    String deviceId;

    @NotBlank
    @Email
    String email;
}
