package com.dburyak.example.jwt.api.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class JwtLoginRequest {

    @NotBlank
    String username;

    @NotBlank
    String password;

    @NotBlank
    String deviceId;

    Boolean rememberMe;
}
