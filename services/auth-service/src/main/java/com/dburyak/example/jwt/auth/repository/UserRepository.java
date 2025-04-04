package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {

    User findByTenantIdAndUsername(String tenantId, String username);

    User findByTenantIdAndUuid(String tenantId, UUID uuid);
}
