package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.api.auth.JwtLoginRequest;
import com.dburyak.example.jwt.api.auth.JwtLoginResponse;
import com.dburyak.example.jwt.api.auth.JwtRefreshTokenRequest;
import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.auth.service.AuthService;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_REFRESH;
import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT;
import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_TOKEN;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping(AUTH_JWT)
@RequiredArgsConstructor
public class JwtTokenController {
    private static final String ERR_INVALID_CREDENTIALS = "Invalid credentials";
    private final AuthService authService;

    @PostMapping(AUTH_JWT_TOKEN)
    @JsonView(READ.class)
    public ResponseEntity<JwtLoginResponse> createToken(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @Validated(CREATE.class) @JsonView(CREATE.class) @RequestBody JwtLoginRequest req) {
        var resp = authService.createJwtToken(tenantUuid, req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(AUTH_JWT_REFRESH)
    @JsonView(READ.class)
    public ResponseEntity<JwtLoginResponse> refreshToken(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @Valid @RequestBody JwtRefreshTokenRequest req) {
        var resp = authService.refreshJwtToken(tenantUuid, req);
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
        // user/refresh-token/whatever not found also means invalid credentials
        return buildInvalidCredsProblemDetail();
    }

    private ProblemDetail buildInvalidCredsProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(UNAUTHORIZED);
        problemDetail.setTitle(ERR_INVALID_CREDENTIALS);
        problemDetail.setDetail(ERR_INVALID_CREDENTIALS);
        return problemDetail;
    }
}
