package com.dburyak.example.jwt.api.common;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = PRIVATE)
@Jacksonized
public class ExternalId {

    @Email(groups = {READ.class, CREATE.class, UPDATE.class})
    @NotBlank(groups = {READ.class, CREATE.class, UPDATE.class})
    String email;
}
