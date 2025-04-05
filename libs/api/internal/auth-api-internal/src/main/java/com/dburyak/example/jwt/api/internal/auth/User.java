package com.dburyak.example.jwt.api.internal.auth;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class User {

    @NotBlank(groups = {READ.class})
    @JsonView({READ.class})
    String tenantId;

    @NotNull(groups = {CREATE.class})
    @JsonView({READ.class, CREATE.class})
    UUID uuid;

    @NotBlank(groups = {CREATE.class, UPDATE.class})
    @JsonView({READ.class, CREATE.class, UPDATE.class})
    String username;

    // of course, there should be a dedicated password validator with annotation used here, skipped for brevity
    @NotBlank(groups = {CREATE.class, UPDATE.class})
    @JsonView({CREATE.class, UPDATE.class})
    String password;

    @JsonView({READ.class, CREATE.class, UPDATE.class})
    Set<String> roles;

    @JsonView({READ.class})
    UUID createdBy;

    @JsonView({READ.class})
    Instant createdDate;

    @JsonView({READ.class})
    UUID lastModifiedBy;

    @JsonView({READ.class})
    Instant lastModifiedDate;
}
