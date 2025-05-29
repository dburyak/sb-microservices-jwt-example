package com.dburyak.example.jwt.lib.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for mongo entity with all features:
 * - db id
 * - business level id
 * - audit
 * - optimistic locking
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class MongoEntity {
    // field names for queries
    public static final String FIELD_TENANT_UUID = "tenantUuid";
    public static final String FIELD_UUID = "uuid";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";
    public static final String FIELD_VERSION = "version";

    @Id
    private String id;

    private UUID tenantUuid;
    private UUID uuid;

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
