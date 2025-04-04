package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepositoryCustom {

    void insertOrReplaceByTenantIdAndUserUuidAndDeviceId(RefreshToken refreshToken);

    boolean replaceOneByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(RefreshToken refreshToken);

    boolean deleteByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(String tenantId, UUID userUuid,
            String deviceId, UUID token);
}
