package com.dburyak.example.jwt.user.repository;

import com.dburyak.example.jwt.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
    void deleteAllByTenantUuid(UUID tenantUuid);

    User findByTenantUuidAndUuid(UUID tenantUuid, UUID uuid);

    @Query("{ 'tenantUuid' : ?0, 'contactInfo.email' : ?1 }")
    boolean existsByContactInfoEmail(UUID tenantUuid, String contactInfoEmail);
}
