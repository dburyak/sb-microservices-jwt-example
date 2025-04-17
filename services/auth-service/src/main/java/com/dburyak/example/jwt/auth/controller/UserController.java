package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.internal.auth.User;
import com.dburyak.example.jwt.auth.service.UserService;
import com.dburyak.example.jwt.lib.auth.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.api.internal.auth.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;

@RestController
@RequestMapping(USERS_ROOT)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Only regular users can be created via this endpoint.
     * Part of the user creation flow by {@link Role#USER_MANAGER}.
     */
    @PostMapping
    public ResponseEntity<User> create(
            @RequestAttribute(TENANT_UUID) @NotBlank UUID tenantUuid,
            @Validated(CREATE.class) @RequestBody User req) {
        var user = userService.create(tenantUuid, req, Set.of(Role.USER));
        return ResponseEntity.ok(user);
    }
}
