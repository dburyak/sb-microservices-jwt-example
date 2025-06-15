package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.UUID;

public interface RegisteredUserOTPRepository
        extends MongoRepository<RegisteredUserOTP, String>, RegisteredUserOTPRepositoryCustom {

    RegisteredUserOTP findByTenantUuidAndUserUuidAndDeviceIdAndTypeAndExpiresAtAfter(UUID tenantUuid, UUID userUuid,
            String deviceId, RegisteredUserOTP.Type type, Instant expiresAtAfter);
}
