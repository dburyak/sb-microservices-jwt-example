package com.dburyak.example.jwt.user.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.api.user.UserWithRoles;
import com.dburyak.example.jwt.user.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.user.PathParams.USERNAME;
import static com.dburyak.example.jwt.api.user.Paths.BY_USERNAME;
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_MANAGEMENT;
import static com.dburyak.example.jwt.api.user.Paths.USER_BY_USERNAME;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;

@RestController
@RequestMapping(PATH_USER_MANAGEMENT)
@RequiredArgsConstructor
public class UserManagementController {
    private final UserService userService;

    /**
     * This endpoint does not require an OTP code for registration, instead it is called by user-manager to onboard
     * users of the tenant one-by-one. Realistically, there also should be similar endpoint for onboarding multiple
     * users at once, for ingesting users from a CSV file, from external systems, or similar.
     */
    @PostMapping
    @JsonView(READ.class)
    public ResponseEntity<User> create(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @Validated(CREATE.class) @RequestBody @NotNull UserWithRoles req) {
        var user = userService.createByManager(tenantUuid, req);
        return ResponseEntity.ok(user);
    }

    @GetMapping(BY_USERNAME + USER_BY_USERNAME)
    @JsonView(READ.class)
    public ResponseEntity<User> findByUsernameByManager(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @PathVariable(USERNAME) @NotBlank String username) {
        var user = userService.findByUsername(tenantUuid, username);
        return ResponseEntity.ok(user);
    }
}
