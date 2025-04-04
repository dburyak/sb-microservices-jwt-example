package com.dburyak.example.jwt.auth.apimodel;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class JwtLoginResponse {
    private UUID userUuid;
    private String accessToken;
    private Instant accessTokenExpiresAt;
    private UUID refreshToken;
    private Instant refreshTokenExpiresAt;
}
