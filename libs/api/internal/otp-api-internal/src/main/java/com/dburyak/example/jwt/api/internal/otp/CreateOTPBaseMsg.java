package com.dburyak.example.jwt.api.internal.otp;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

@Value
@NonFinal
@SuperBuilder(toBuilder = true)
public abstract class CreateOTPBaseMsg {

    @NotNull
    protected OTPType type;

    protected String locale;
}
