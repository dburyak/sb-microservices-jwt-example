package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.internal.auth.User;
import com.dburyak.example.jwt.auth.service.UserService;
import com.dburyak.example.jwt.lib.auth.Role;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.api.auth.Paths.USER_PASSWORD;
import static com.dburyak.example.jwt.api.auth.Paths.USER_PASSWORD_RESET;
import static com.dburyak.example.jwt.api.auth.Paths.USER_PASSWORD_RESET_OTP;
import static com.dburyak.example.jwt.api.common.PathParams.DEVICE_ID;
import static com.dburyak.example.jwt.api.common.Paths.ANONYMOUS;
import static com.dburyak.example.jwt.api.common.Paths.DEVICES;
import static com.dburyak.example.jwt.api.common.Paths.DEVICE_BY_ID;
import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;

@RestController
@RequestMapping(USERS)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Internal endpoint, not exposed to the public.
     * Only regular users can be created via this endpoint.
     * Part of the user creation flow by {@link Role#USER_MANAGER}.
     */
    @PostMapping
    @JsonView(READ.class)
    public ResponseEntity<User> create(
            @RequestAttribute(TENANT_UUID) @NotBlank UUID tenantUuid,
            @Validated(CREATE.class) @RequestBody User req) {
        var user = userService.create(tenantUuid, req, Set.of(Role.USER));
        return ResponseEntity.ok(user);
    }

    @PostMapping(ANONYMOUS + DEVICES + DEVICE_BY_ID + USER_PASSWORD + USER_PASSWORD_RESET + USER_PASSWORD_RESET_OTP)
    @JsonView(READ.class)
    public ResponseEntity<Void> createPasswordResetOTP(
            @RequestAttribute(TENANT_UUID) @NotBlank UUID tenantUuid,
            @PathVariable(DEVICE_ID) @NotBlank String deviceId,
            @Validated(CREATE.class) @RequestBody ExternalId req) {
        userService.createPasswordResetOTP(tenantUuid, deviceId, req);
        return ResponseEntity.ok().build();
    }
}
