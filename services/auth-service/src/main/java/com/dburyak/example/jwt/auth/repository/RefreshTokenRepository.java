package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String>, RefreshTokenRepositoryCustom {
    void deleteAllByTenantUuid(UUID tenantUuid);
}
