package com.dburyak.example.jwt.api.auth;

import com.dburyak.example.jwt.api.common.ExternalId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreatePasswordResetOTPRequest {

    String locale;

    @NotNull
    ExternalId externalId;

    @NotBlank
    String deviceId;
}
