package com.dburyak.example.jwt.tenant.repository;

import com.dburyak.example.jwt.tenant.domain.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TenantRepository extends MongoRepository<Tenant, String>, TenantRepositoryCustom {
    Tenant findByUuid(UUID uuid);

    long deleteByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    boolean existsByName(String name);

    boolean existsByUuidAndName(UUID uuid, String name);

    Tenant findByName(String name);
}
