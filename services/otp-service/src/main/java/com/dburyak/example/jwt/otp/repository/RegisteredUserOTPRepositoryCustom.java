package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP;

import java.time.Instant;
import java.util.UUID;

public interface RegisteredUserOTPRepositoryCustom {

    RegisteredUserOTP insertOrReplaceByTenantUuidAndUserUuidAndDeviceIdAndType(RegisteredUserOTP otp);

    RegisteredUserOTP findAndDeleteByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCodeAndExpiresAtAfter(UUID tenantUuid,
            UUID userUuid, String deviceId, RegisteredUserOTP.Type type, String code, Instant expiresAtAfter);
}
