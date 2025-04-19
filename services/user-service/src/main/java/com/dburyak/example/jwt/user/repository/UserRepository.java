package com.dburyak.example.jwt.user.repository;

import com.dburyak.example.jwt.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {
    void deleteAllByTenantUuid(UUID tenantUuid);
}
