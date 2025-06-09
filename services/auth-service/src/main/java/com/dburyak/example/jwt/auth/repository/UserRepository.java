package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    User findByTenantUuidAndUsername(UUID tenantUuid, String username);

    User findByTenantUuidAndUuid(UUID tenantUuid, UUID uuid);

    @Query("{ 'tenantUuid': ?0, 'externalId.email': ?1 }")
    User findByTenantUuidAndExternalIdEmail(UUID tenantUuid, String email);

    void deleteAllByTenantUuid(UUID tenantUuid);
}
