package com.dburyak.example.jwt.auth.apimodel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtLoginWithUsernameAndPasswordRequest {
    @NotBlank
    private String tenantId;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String deviceId;

    private Boolean rememberMe;
}
