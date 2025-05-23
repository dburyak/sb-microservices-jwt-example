package com.dburyak.example.jwt.user.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.api.user.CreateRegistrationOtpViaEmail;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.user.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.common.PathParams.USER_UUID;
import static com.dburyak.example.jwt.api.common.Paths.USERS;
import static com.dburyak.example.jwt.api.common.Paths.USER_BY_UUID;
import static com.dburyak.example.jwt.api.user.Paths.CONTACT_INFO;
import static com.dburyak.example.jwt.api.user.Paths.REGISTRATION_OTP;
import static com.dburyak.example.jwt.api.user.QueryParams.REGISTRATION_CODE;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;

@RestController
@RequestMapping(USERS)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(REGISTRATION_OTP)
    public ResponseEntity<Void> createRegistrationOtp(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @Valid @RequestBody @NotNull CreateRegistrationOtpViaEmail req) {
        userService.createRegistrationOtp(tenantUuid, req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @JsonView(READ.class)
    public ResponseEntity<User> create(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @RequestParam(REGISTRATION_CODE) @NotBlank String registrationCode,
            @Validated(CREATE.class) @RequestBody @NotNull User req) {
        var user = userService.createViaRegistration(tenantUuid, req, registrationCode);
        return ResponseEntity.ok(user);
    }

    @GetMapping(USER_BY_UUID)
    @JsonView(READ.class)
    public ResponseEntity<User> findByUuid(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @PathVariable(USER_UUID) @NotNull UUID userUuid) {
        var user = userService.findByUuid(tenantUuid, userUuid);
        return ResponseEntity.ok(user);
    }

    @GetMapping(USER_BY_UUID + CONTACT_INFO)
    @JsonView(READ.class)
    public ResponseEntity<ContactInfo> getContactInfo(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @PathVariable(USER_UUID) @NotNull UUID userUuid) {
        var contactInfo = userService.findContactInfoByUuid(tenantUuid, userUuid);
        return ResponseEntity.ok(contactInfo);
    }
}
