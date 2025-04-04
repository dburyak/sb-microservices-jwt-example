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

    public JwtLoginResponse createJwtToken(String tenantId, JwtLoginWithUsernameAndPasswordRequest req) {
        var user = userRepository.findByTenantIdAndUsername(tenantId, req.getUsername());
        if (user == null) {
            throw new NotFoundException(String.format("User(username=%s)", req.getUsername()));
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password for user: " + req.getUsername());
        }
        var accessToken = jwtGenerator.generateUserToken(tenantId, user.getUuid(), req.getDeviceId(), user.getRoles());
        var resp = JwtLoginResponse.builder()
                .userUuid(user.getUuid())
                .accessToken(accessToken)
                .accessTokenExpiresAt(Instant.now().plus(jwtAuthProps.getGenerator().getTtl()));
        if (req.getRememberMe() == null || req.getRememberMe()) { // rememberMe=true if not specified explicitly
            var refreshToken = RefreshToken.builder()
                    .tenantId(tenantId)
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

    public JwtLoginResponse refreshJwtToken(String tenantId, JwtRefreshTokenRequest req) {
        var found = false;
        var resp = JwtLoginResponse.builder()
                .userUuid(req.getUserUuid());
        if (req.getRememberMe()) { // generate a new refresh-token, and replace the current one
            var newRefreshToken = RefreshToken.builder()
                    .tenantId(tenantId)
                    .userUuid(req.getUserUuid())
                    .deviceId(req.getDeviceId())
                    .token(UUID.randomUUID())
                    .expiresAt(Instant.now().plus(jwtAuthProps.getGenerator().getRefreshTokenTtl()))
                    .build();
            found = refreshTokenRepository.replaceOneByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(
                    newRefreshToken);
            if (found) {
                resp.refreshToken(newRefreshToken.getToken())
                        .accessTokenExpiresAt(newRefreshToken.getExpiresAt());
            }
        } else { // delete current refresh-token
            found = refreshTokenRepository.deleteByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(
                    tenantId, req.getUserUuid(), req.getDeviceId(), req.getRefreshToken());
        }
        if (!found) {
            throw new NotFoundException(String.format("RefreshToken(token=%s)", req.getRefreshToken()));
        }
        var user = userRepository.findByTenantIdAndUuid(tenantId, req.getUserUuid());
        if (user == null) {
            throw new NotFoundException(String.format("User(uuid=%s)", req.getUserUuid()));
        }
        var accessToken = jwtGenerator.generateUserToken(tenantId, user.getUuid(), req.getDeviceId(), user.getRoles());
        return resp.accessToken(accessToken)
                .accessTokenExpiresAt(Instant.now().plus(jwtAuthProps.getGenerator().getTtl()))
                .build();
    }
}
