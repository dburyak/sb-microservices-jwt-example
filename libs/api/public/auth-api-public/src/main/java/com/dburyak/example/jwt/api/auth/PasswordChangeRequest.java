package com.dburyak.example.jwt.api.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class PasswordChangeRequest {

    @NotBlank
    String otp;

    @NotBlank
    String oldPassword;

    @NotBlank
    String newPassword;
}
