package com.dburyak.example.jwt.user.repository;

import com.dburyak.example.jwt.user.domain.ContactInfo;

import java.util.UUID;

public interface UserRepositoryCustom {
    ContactInfo findContactInfoByTenantUuidAndUuid(UUID tenantUuid, UUID uuid);
}
