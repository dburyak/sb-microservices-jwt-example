package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.auth.apimodel.JwtLoginResponse;
import com.dburyak.example.jwt.auth.apimodel.JwtLoginWithUsernameAndPasswordRequest;
import com.dburyak.example.jwt.auth.apimodel.JwtRefreshTokenRequest;
import com.dburyak.example.jwt.auth.service.AuthService;
import com.dburyak.example.jwt.lib.auth.Attributes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/jwt")
@RequiredArgsConstructor
public class JwtTokenController {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<JwtLoginResponse> createToken(
            @NotBlank @RequestAttribute(Attributes.TENANT_ID) String tenantId,
            @Valid @RequestBody JwtLoginWithUsernameAndPasswordRequest req) {
        var resp = authService.createJwtToken(tenantId, req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtLoginResponse> refreshToken(
            @NotBlank @RequestAttribute(Attributes.TENANT_ID) String tenantId,
            @Valid @RequestBody JwtRefreshTokenRequest req) {
        var resp = authService.refreshJwtToken(tenantId, req);
        return ResponseEntity.ok(resp);
    }
}
