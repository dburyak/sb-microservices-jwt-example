package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.auth.apimodel.JwtLoginResponse;
import com.dburyak.example.jwt.auth.apimodel.JwtLoginWithUsernameAndPasswordRequest;
import com.dburyak.example.jwt.auth.apimodel.JwtRefreshTokenRequest;
import com.dburyak.example.jwt.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
            @Valid @RequestBody JwtLoginWithUsernameAndPasswordRequest req) {
        var resp = authService.createJwtToken(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtLoginResponse> refreshToken(@Valid @RequestBody JwtRefreshTokenRequest req) {
        var resp = authService.refreshJwtToken(req);
        return ResponseEntity.ok(resp);
    }
}
