package com.dburyak.example.jwt.api.internal.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateOTPMsg {

    @NotNull
    OTPType type;

    @NotBlank
    @Email
    String email;

    String locale;
}
