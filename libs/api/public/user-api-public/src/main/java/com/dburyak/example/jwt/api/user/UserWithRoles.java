package com.dburyak.example.jwt.api.user;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class UserWithRoles extends User {

    @NotNull(groups = {CREATE.class, UPDATE.class})
    @JsonView({READ.class, CREATE.class, UPDATE.class})
    Set<String> roles;
}
