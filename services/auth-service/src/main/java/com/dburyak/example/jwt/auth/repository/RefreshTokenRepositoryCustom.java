package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepositoryCustom {

    void insertOrReplaceByTenantUuidAndUserUuidAndDeviceId(RefreshToken refreshToken);

    boolean replaceOneByTenantUuidAndUserUuidAndDeviceIdAndTokenAndNotExpired(RefreshToken refreshToken);

    boolean deleteByTenantUuidAndUserUuidAndDeviceIdAndTokenAndNotExpired(UUID tenantUuid, UUID userUuid,
            String deviceId, UUID token);
}
