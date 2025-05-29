package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP;

import java.time.Instant;
import java.util.UUID;

public interface ExternallyIdentifiedOTPRepositoryCustom {

    ExternallyIdentifiedOTP insertOrReplaceByTenantUuidAndEmailAndDeviceIdAndType(ExternallyIdentifiedOTP otp);

    ExternallyIdentifiedOTP findAndDeleteByTenantUuidAndEmailAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(
            UUID tenantUuid, String email, String deviceId, ExternallyIdentifiedOTP.Type type, String code,
            Instant expiresAtBefore);
}
