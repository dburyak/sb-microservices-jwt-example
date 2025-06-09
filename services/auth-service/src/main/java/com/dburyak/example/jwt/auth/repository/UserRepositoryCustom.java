package com.dburyak.example.jwt.auth.repository;

import java.util.UUID;

public interface UserRepositoryCustom {
    boolean updatePasswordByExternalIdEmail(UUID tenantUuid, String email, String newPassword);
}
