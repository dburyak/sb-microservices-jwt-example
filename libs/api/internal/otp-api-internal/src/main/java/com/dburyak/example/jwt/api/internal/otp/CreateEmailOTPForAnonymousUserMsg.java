package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.ExternalId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * Create email OTP request message originating from an anonymous user (not registered or not authenticated). In this
 * case, it is identified by the externalId (email, phone, google-id, etc.). And the credentials passed in the
 * request have nothing to do with the user, they belong to the service making the request on behalf of the anonymous
 * user.
 * Example scenarios: registration, password reset (forgot password flow).
 */
@Value
@Builder(toBuilder = true)
public class CreateEmailOTPForAnonymousUserMsg {

    String locale;

    @NotNull
    UUID tenantUuid;

    @NotNull
    ExternalId externalId;

    @NotBlank
    String deviceId;

    @NotNull
    ExternallyIdentifiedOTP.Type type;
}
