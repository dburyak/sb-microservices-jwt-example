package com.dburyak.example.jwt.user.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.user.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dburyak.example.jwt.api.user.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_ID;

@RestController
@RequestMapping(USERS_ROOT)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(
            @NotBlank @RequestAttribute(TENANT_ID) String tenantId,
            @Validated(CREATE.class) @RequestBody User req) {
        var user = userService.create(tenantId, req);
        return ResponseEntity.ok(user);
    }
}
