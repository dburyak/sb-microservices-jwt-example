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
