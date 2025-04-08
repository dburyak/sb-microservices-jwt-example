package com.dburyak.example.jwt.api.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class JwtRefreshTokenRequest {
    @NotNull
    UUID userUuid;

    @NotBlank
    String deviceId;

    @NotNull
    UUID refreshToken;

    Boolean rememberMe;
}
