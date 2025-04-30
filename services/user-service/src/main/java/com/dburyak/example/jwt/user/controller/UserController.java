package com.dburyak.example.jwt.user.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.user.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.user.Paths.CONTACT_INFO;
import static com.dburyak.example.jwt.api.user.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.api.user.Paths.USER_BY_UUID;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;

@RestController
@RequestMapping(USERS_ROOT)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(
            @NotBlank @RequestAttribute(TENANT_UUID) UUID tenantUuid,
            @Validated(CREATE.class) @RequestBody User req) {
        var user = userService.create(tenantUuid, req);
        return ResponseEntity.ok(user);
    }

    @GetMapping(USER_BY_UUID)
    public ResponseEntity<User> findByUuid(
            @NotBlank @RequestAttribute(TENANT_UUID) UUID tenantUuid,
            @NotBlank UUID userUuid) {
        var user = userService.findByUuid(tenantUuid, userUuid);
        return ResponseEntity.ok(user);
    }

    @GetMapping(USER_BY_UUID + CONTACT_INFO)
    public ResponseEntity<ContactInfo> getContactInfo(
            @NotBlank @RequestAttribute(TENANT_UUID) UUID tenantUuid,
            @NotBlank UUID userUuid) {
        var contactInfo = userService.findContactInfoByUuid(tenantUuid, userUuid);
        return ResponseEntity.ok(contactInfo);
    }
}
