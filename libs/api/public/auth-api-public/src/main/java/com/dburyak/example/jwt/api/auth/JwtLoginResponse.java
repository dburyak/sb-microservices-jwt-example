package com.dburyak.example.jwt.api.auth;

import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class JwtLoginResponse {

    @JsonView(READ.class)
    UUID userUuid;

    @JsonView(READ.class)
    String accessToken;

    @JsonView(READ.class)
    Instant accessTokenExpiresAt;

    @JsonView(READ.class)
    UUID refreshToken;

    @JsonView(READ.class)
    Instant refreshTokenExpiresAt;
}
