package com.dburyak.example.jwt.tenant.service.converter;

import com.dburyak.example.jwt.api.tenant.Tenant;
import org.springframework.stereotype.Component;

@Component
public class TenantConverter {

    public com.dburyak.example.jwt.tenant.domain.Tenant toDomain(Tenant tenant) {
        return com.dburyak.example.jwt.tenant.domain.Tenant.builder()
                .uuid(tenant.getUuid())
                .name(tenant.getName())
                .description(tenant.getDescription())
                .contactEmails(tenant.getContactEmails())
                .createdDate(tenant.getCreatedDate())
                .createdBy(tenant.getCreatedBy())
                .build();
    }

    public Tenant toApiModel(com.dburyak.example.jwt.tenant.domain.Tenant tenant) {
        return Tenant.builder()
                .uuid(tenant.getUuid())
                .name(tenant.getName())
                .description(tenant.getDescription())
                .contactEmails(tenant.getContactEmails())
                .createdDate(tenant.getCreatedDate())
                .createdBy(tenant.getCreatedBy())
                .lastModifiedDate(tenant.getLastModifiedDate())
                .lastModifiedBy(tenant.getLastModifiedBy())
                .build();
    }
}
