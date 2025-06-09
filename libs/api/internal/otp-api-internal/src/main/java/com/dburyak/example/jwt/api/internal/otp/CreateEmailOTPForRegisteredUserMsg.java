package com.dburyak.example.jwt.api.internal.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

/**
 * Create OTP request message originating from a registered user. Example scenarios: change password, change email,
 * change phone number, modify payment methods, etc. Such a request is always associated (belongs to) with a particular
 * authenticated user, i.e. credentials passed in the request belong to the calling user (owner of the OTP to be
 * created).
 */
@Value
@Builder
public class CreateEmailOTPForRegisteredUserMsg {

    String locale;

    @NotNull
    RegisteredUserOTP.Type type;

    @NotBlank
    @Email
    String email;

    // User identifiers (tenantUuid+userUuid) are in the request credentials.
}
