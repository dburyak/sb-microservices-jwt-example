package com.dburyak.example.jwt.api.auth;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class JwtLoginResponse {
    UUID userUuid;
    String accessToken;
    Instant accessTokenExpiresAt;
    UUID refreshToken;
    Instant refreshTokenExpiresAt;
}
