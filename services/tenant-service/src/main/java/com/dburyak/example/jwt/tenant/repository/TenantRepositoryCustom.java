package com.dburyak.example.jwt.tenant.repository;

import java.util.UUID;

public interface TenantRepositoryCustom {

    /**
     * Deletes a tenant by its name.
     *
     * @param name the name of the tenant to delete
     *
     * @return UUID of the deleted tenant, or null if no tenant was found with the given name
     */
    UUID deleteByName(String name);
}
