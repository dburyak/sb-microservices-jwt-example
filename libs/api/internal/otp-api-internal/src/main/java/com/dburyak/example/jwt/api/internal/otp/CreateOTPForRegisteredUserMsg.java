package com.dburyak.example.jwt.api.internal.otp;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

/**
 * Create OTP request message originating from a registered user. Example scenarios: reset password, change email,
 * change phone number, modify payment methods, etc. Such a request is always associated (belongs to) with a particular
 * user.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class CreateOTPForRegisteredUserMsg extends CreateOTPBaseMsg {
    // User identifiers (tenantUuid+userUuid) are in the request credentials. And otp delivery address can be fetched
    // from user-service.
}
