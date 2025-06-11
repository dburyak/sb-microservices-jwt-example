package com.dburyak.example.jwt.auth.repository;

import java.util.UUID;

public interface UserRepositoryCustom {
    boolean updatePasswordByExternalIdEmail(UUID tenantUuid, String email, String newPassword);
    boolean updatePasswordByUuid(UUID tenantUuid, UUID uuid, String newPassword);
}
