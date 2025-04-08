package com.dburyak.example.jwt.tenant.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.tenant.domain.Tenant.COLLECTION;

@Document(collection = COLLECTION)
public class Tenant {
    public static final String COLLECTION = "tenants";

    private String tenantId;
    private Set<String> contactEmails;

    @CreatedBy
    private UUID createdBy;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedBy
    private UUID lastModifiedBy;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Integer version;
}
