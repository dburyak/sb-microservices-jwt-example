package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.OTP;
import com.dburyak.example.jwt.otp.domain.OTPType;

import java.time.Instant;
import java.util.UUID;

public interface OTPRepositoryCustom {
    OTP insertOrReplaceByTenantUuidAndUserUuidAndDeviceIdAndType(OTP otp);

    OTP insertOrReplaceByTenantUuidAndExternalIdAndDeviceIdAndType(OTP otp);

    OTP findAndDeleteByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(UUID tenantUuid, UUID userUuid,
            String deviceId, OTPType type, String code, Instant expiresAtBefore);

    OTP findAndDeleteByTenantUuidAndExternalIdAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(UUID tenantUuid,
            String externalId, String deviceId, OTPType type, String code, Instant expiresAtBefore);
}
