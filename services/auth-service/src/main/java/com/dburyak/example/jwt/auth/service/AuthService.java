package com.dburyak.example.jwt.auth.service;

import com.dburyak.example.jwt.auth.apimodel.JwtLoginResponse;
import com.dburyak.example.jwt.auth.apimodel.JwtLoginWithUsernameAndPasswordRequest;
import com.dburyak.example.jwt.auth.apimodel.JwtRefreshTokenRequest;
import com.dburyak.example.jwt.auth.domain.RefreshToken;
import com.dburyak.example.jwt.auth.repository.RefreshTokenRepository;
import com.dburyak.example.jwt.auth.repository.UserRepository;
import com.dburyak.example.jwt.lib.auth.JwtGenerator;
import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final JwtAuthProperties jwtAuthProps;

    public JwtLoginResponse createJwtToken(JwtLoginWithUsernameAndPasswordRequest req) {
        var user = userRepository.findByTenantIdAndUsername(req.getTenantId(), req.getUsername());
        if (user == null) {
            throw new NotFoundException(String.format("User(username=%s)", req.getUsername()));
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password for user: " + req.getUsername());
        }
        var accessToken = jwtGenerator.generate(req.getTenantId(), user.getUuid().toString(), user.getRoles());
        var resp = JwtLoginResponse.builder()
                .userUuid(user.getUuid())
                .accessToken(accessToken)
                .accessTokenExpiresAt(Instant.now().plus(jwtAuthProps.getGenerator().getTtl()));
        if (req.getRememberMe() == null || req.getRememberMe()) { // rememberMe=true if not specified explicitly
            var refreshToken = RefreshToken.builder()
                    .tenantId(req.getTenantId())
                    .userUuid(user.getUuid())
                    .deviceId(req.getDeviceId())
                    .token(UUID.randomUUID())
                    .expiresAt(Instant.now().plus(jwtAuthProps.getGenerator().getRefreshTokenTtl()))
                    .build();
            refreshTokenRepository.insertOrReplaceByTenantIdAndUserUuidAndDeviceId(refreshToken);
            resp.refreshToken(refreshToken.getToken())
                    .refreshTokenExpiresAt(refreshToken.getExpiresAt());
        }
        return resp.build();
    }

    public JwtLoginResponse refreshJwtToken(JwtRefreshTokenRequest req) {
        if (req.getRememberMe()) { // generate new refresh-token, and replace the current one
            var newRefreshToken = RefreshToken.builder()
                    .tenantId(req.getTenantId())
                    .userUuid(req.getUserUuid())
                    .deviceId(req.getDeviceId())
                    .token(UUID.randomUUID())
                    .expiresAt(Instant.now().plus(jwtAuthProps.getGenerator().getRefreshTokenTtl()))
                    .build();
        } else { // delete current refresh-token
        }
    }
}
