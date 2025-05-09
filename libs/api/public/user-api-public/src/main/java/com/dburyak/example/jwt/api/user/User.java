package com.dburyak.example.jwt.api.user;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
//@Jacksonized
public class User {

    @JsonView({READ.class})
    UUID uuid;

    @NotBlank(groups = {CREATE.class})
    @JsonView({CREATE.class})
    String username;

    @NotBlank(groups = {CREATE.class})
    @JsonView({CREATE.class})
    String password;

    @NotBlank(groups = {CREATE.class, UPDATE.class})
    @JsonView({READ.class, CREATE.class, UPDATE.class})
    String displayName;

    @NotBlank(groups = {CREATE.class, UPDATE.class})
    @JsonView({READ.class, CREATE.class, UPDATE.class})
    String profileIcon;

    @NotNull(groups = {CREATE.class, UPDATE.class})
    ContactInfo contactInfo;

    @JsonView({READ.class})
    UUID createdBy;

    @JsonView({READ.class})
    Instant createdDate;

    @JsonView({READ.class})
    UUID lastModifiedBy;

    @JsonView({READ.class})
    Instant lastModifiedDate;
}
