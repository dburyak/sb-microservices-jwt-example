package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.api.auth.CreatePasswordResetOTPRequest;
import com.dburyak.example.jwt.api.auth.PasswordChangeRequest;
import com.dburyak.example.jwt.api.auth.PasswordResetRequest;
import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.dburyak.example.jwt.api.internal.auth.User;
import com.dburyak.example.jwt.auth.service.UserService;
import com.dburyak.example.jwt.lib.auth.Role;
import com.dburyak.example.jwt.lib.req.Attributes;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.api.auth.Paths.OTP;
import static com.dburyak.example.jwt.api.auth.Paths.USER_PASSWORD;
import static com.dburyak.example.jwt.api.auth.Paths.USER_PASSWORD_CHANGE;
import static com.dburyak.example.jwt.api.auth.Paths.USER_PASSWORD_RESET;
import static com.dburyak.example.jwt.api.common.Paths.ANONYMOUS;
import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;
import static com.dburyak.example.jwt.lib.req.Attributes.USER_UUID;

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

    /**
     * Create password reset OTP for unauthenticated user. Used for "Forgot Password" flow.
     */
    @PostMapping(ANONYMOUS + USER_PASSWORD + USER_PASSWORD_RESET + OTP)
    public ResponseEntity<Void> createPasswordResetOTP(
            @RequestAttribute(TENANT_UUID) @NotBlank UUID tenantUuid,
            @Validated(CREATE.class) @RequestBody CreatePasswordResetOTPRequest req) {
        userService.createPasswordResetOTP(tenantUuid, req);
        return ResponseEntity.ok().build();
    }

    @PutMapping(ANONYMOUS + USER_PASSWORD + USER_PASSWORD_RESET)
    public ResponseEntity<Void> resetPassword(
            @RequestAttribute(TENANT_UUID) @NotBlank UUID tenantUuid,
            @Validated(UPDATE.class) @RequestBody PasswordResetRequest req) {
        userService.resetPassword(tenantUuid, req);
        return ResponseEntity.ok().build();
    }

    /**
     * Create password change OTP for authenticated user. Used for "Change Password" flow.
     */
    @PostMapping(USER_PASSWORD + USER_PASSWORD_CHANGE + OTP)
    public ResponseEntity<Void> createPasswordChangeOTP(
            @RequestAttribute(TENANT_UUID) @NotBlank UUID tenantUuid,
            @RequestAttribute(USER_UUID) @NotBlank UUID userUuid) {
        userService.createPasswordChangeOTP(tenantUuid, userUuid);
        return ResponseEntity.ok().build();
    }

    @PutMapping(USER_PASSWORD + USER_PASSWORD_CHANGE)
    public ResponseEntity<Void> changePassword(
            @RequestAttribute(TENANT_UUID) @NotBlank UUID tenantUuid,
            @RequestAttribute(USER_UUID) @NotBlank UUID userUuid,
            @RequestAttribute(Attributes.DEVICE_ID) @NotBlank String deviceId,
            @Validated(UPDATE.class) @RequestBody PasswordChangeRequest req) {
        userService.changePassword(tenantUuid, userUuid, deviceId, req);
        return ResponseEntity.ok().build();
    }
}
