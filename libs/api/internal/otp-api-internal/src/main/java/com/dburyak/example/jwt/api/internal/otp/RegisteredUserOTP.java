package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class RegisteredUserOTP extends OTPBase {

    @JsonView({READ.class})
    UUID userUuid;

    @JsonView({READ.class})
    Type type;

    public enum Type {

        /**
         * OTP for resetting password in case when the user is logged in (change password flow).
         */
        PASSWORD_RESET
    }
}
