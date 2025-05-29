package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface ExternallyIdentifiedOTPRepository
        extends MongoRepository<ExternallyIdentifiedOTP, String>, ExternallyIdentifiedOTPRepositoryCustom {

    @Query("{'externalId.email': ?1, 'deviceId': ?2, 'type': ?3, 'tenantUuid': ?0, 'expiresAt': {$lt: ?4}}")
    ExternallyIdentifiedOTP findByTenantUuidAndEmailAndDeviceIdAndTypeAndExpiresAtBefore(UUID tenantUuid, String email,
            String deviceId, ExternallyIdentifiedOTP.Type type, Instant expiresAtBefore);
}
