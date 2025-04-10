package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {

    User findByTenantUuidAndUsername(UUID tenantUuid, String username);

    User findByTenantUuidAndUuid(UUID tenantUuid, UUID uuid);
}
