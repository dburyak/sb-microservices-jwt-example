package com.dburyak.example.jwt.api.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String deviceId;

    private Boolean rememberMe;
}
