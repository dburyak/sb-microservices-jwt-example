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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.api.internal.auth.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.lib.req.QueryParams.TENANT_UUID;

@RestController
@RequestMapping(USERS_ROOT)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Only regular users can be created by this endpoint.
     * Part of the regular self-registration flows.
     * It is supposed to be called with a service token.
     *
     * @param tenantUuid tenantUuid of the user being created. This is NOT the tenantUuid of the caller. Caller
     *         is assumed to be a service with a service token that has tenantUuid
     *         {@link com.dburyak.example.jwt.lib.req.ReservedIdentifiers#SERVICE_TENANT_UUID}
     */
    @PostMapping
    public ResponseEntity<User> create(
            @RequestParam(name = TENANT_UUID) @NotBlank UUID tenantUuid,
            @Validated(CREATE.class) @RequestBody User req) {
        var user = userService.create(tenantUuid, req, Set.of(Role.USER));
        return ResponseEntity.ok(user);
    }
}
