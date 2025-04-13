package com.dburyak.example.jwt.api.tenant;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.dburyak.example.jwt.api.common.AuditedApiModel;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class Tenant extends AuditedApiModel {

    @NotBlank(groups = {CREATE.class})
    @JsonView({READ.class, CREATE.class})
    String name;

    @JsonView({READ.class, CREATE.class, UPDATE.class})
    String description;

    @JsonView({READ.class, CREATE.class, UPDATE.class})
    Set<@Email(groups = {CREATE.class, UPDATE.class}) String> contactEmails;

    @NotBlank(groups = {CREATE.class})
    @Email(groups = {CREATE.class})
    @JsonView({CREATE.class})
    String adminEmail;
}
