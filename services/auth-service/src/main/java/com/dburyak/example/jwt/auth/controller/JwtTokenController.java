package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.api.auth.JwtLoginRequest;
import com.dburyak.example.jwt.api.auth.JwtLoginResponse;
import com.dburyak.example.jwt.api.auth.JwtRefreshTokenRequest;
import com.dburyak.example.jwt.auth.service.AuthService;
import com.dburyak.example.jwt.lib.auth.Attributes;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/auth/jwt")
@RequiredArgsConstructor
public class JwtTokenController {
    private static final String ERR_INVALID_CREDENTIALS = "Invalid credentials";
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<JwtLoginResponse> createToken(
            @NotBlank @RequestAttribute(Attributes.TENANT_ID) String tenantId,
            @Valid @RequestBody JwtLoginRequest req) {
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

    @ExceptionHandler
    @ResponseStatus(UNAUTHORIZED)
    public ProblemDetail onBadCreds(BadCredentialsException err) {
        return buildInvalidCredsProblemDetail();
    }

    @ExceptionHandler
    @ResponseStatus(UNAUTHORIZED)
    public ProblemDetail onNotFound(NotFoundException err) {
        return buildInvalidCredsProblemDetail();
    }

    private ProblemDetail buildInvalidCredsProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(UNAUTHORIZED);
        problemDetail.setTitle(ERR_INVALID_CREDENTIALS);
        problemDetail.setDetail(ERR_INVALID_CREDENTIALS);
        return problemDetail;
    }
}
