package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.RefreshToken;

public interface RefreshTokenRepositoryCustom {
    void insertOrReplaceByTenantIdAndUserUuidAndDeviceId(RefreshToken refreshToken);
    boolean replaceOneByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(RefreshToken refreshToken);
    boolean deleteByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(RefreshToken refreshToken);
}
