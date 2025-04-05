package com.dburyak.example.jwt.api.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class JwtRefreshTokenRequest {
    @NotNull
    private UUID userUuid;

    @NotBlank
    private String deviceId;

    @NotNull
    private UUID refreshToken;

    private Boolean rememberMe;
}
