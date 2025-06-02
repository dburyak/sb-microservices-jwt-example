package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ExternalId;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class ExternallyIdentifiedOTP extends OTPBase {

    @JsonView({READ.class})
    ExternalId externalId;

    @JsonView({READ.class})
    Type type;

    public enum Type {
        REGISTRATION_WITH_EMAIL,

        /**
         * OTP for resetting password in case when the user is not logged in (forgot password flow).
         */
        PASSWORD_RESET,
    }

}
