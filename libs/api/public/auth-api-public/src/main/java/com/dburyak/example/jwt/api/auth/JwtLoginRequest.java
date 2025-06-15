package com.dburyak.example.jwt.api.auth;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class JwtLoginRequest {

    @NotBlank
    @JsonView({CREATE.class})
    String username;

    @NotBlank
    @JsonView({CREATE.class})
    String password;

    @NotBlank
    @JsonView({CREATE.class})
    String deviceId;

    @JsonView({CREATE.class})
    Boolean rememberMe;
}
