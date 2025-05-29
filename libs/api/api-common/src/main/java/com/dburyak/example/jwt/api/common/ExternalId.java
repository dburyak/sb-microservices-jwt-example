package com.dburyak.example.jwt.api.common;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class ExternalId {

    @Email(groups = {READ.class, CREATE.class, UPDATE.class})
    @NotBlank(groups = {READ.class, CREATE.class, UPDATE.class})
    String email;
}
