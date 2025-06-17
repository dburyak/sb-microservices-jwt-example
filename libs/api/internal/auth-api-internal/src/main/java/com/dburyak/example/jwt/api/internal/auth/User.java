package com.dburyak.example.jwt.api.internal.auth;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.dburyak.example.jwt.api.common.AuditedApiModel;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class User extends AuditedApiModel {

    @NotBlank(groups = {CREATE.class, UPDATE.class})
    @JsonView({READ.class, CREATE.class, UPDATE.class})
    String username;

    @JsonView({CREATE.class, UPDATE.class})
    String password;

    @JsonView({READ.class, CREATE.class, UPDATE.class})
    Set<String> roles;
}
