package com.dburyak.example.jwt.api.user;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.dburyak.example.jwt.api.common.AuditedApiModel;
import com.dburyak.example.jwt.api.common.ExternalId;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@NonFinal
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class User extends AuditedApiModel {

    @NotNull(groups = {CREATE.class, UPDATE.class})
    @JsonView({READ.class, CREATE.class, UPDATE.class})
    ExternalId externalId;

    @NotBlank(groups = {CREATE.class, UPDATE.class})
    @JsonView({READ.class, CREATE.class, UPDATE.class})
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
    @JsonView({READ.class, CREATE.class, UPDATE.class})
    ContactInfo contactInfo;
}
