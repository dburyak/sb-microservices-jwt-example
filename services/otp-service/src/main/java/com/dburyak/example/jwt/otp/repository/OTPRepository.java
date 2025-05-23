package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.OTP;
import com.dburyak.example.jwt.otp.domain.OTPType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.UUID;

public interface OTPRepository extends MongoRepository<OTP, String>, OTPRepositoryCustom {
    OTP findByTenantUuidAndUserUuidAndDeviceIdAndTypeAndExpiresAtBefore(UUID tenantUuid, UUID userUuid, String deviceId,
            OTPType type, Instant expiresAtBefore);

    OTP findByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(UUID tenantUuid, UUID userUuid,
            String deviceId, OTPType type, String code, Instant expiresAtBefore);

    OTP findByTenantUuidAndExternalIdAndDeviceIdAndTypeAndExpiresAtBefore(UUID tenantUuid, String externalId,
            String deviceId, OTPType type, Instant expiresAtBefore);
}
