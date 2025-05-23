package com.dburyak.example.jwt.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateRegistrationOtpViaEmail {

    @Email
    @NotBlank
    String email;

    @NotBlank
    String locale;

    @NotBlank
    String deviceId;
}
