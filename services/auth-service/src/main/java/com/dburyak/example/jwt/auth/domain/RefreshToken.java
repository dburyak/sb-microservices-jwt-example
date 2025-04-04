package com.dburyak.example.jwt.auth.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

import static com.dburyak.example.jwt.auth.domain.RefreshToken.COLLECTION;

@Document(collection = COLLECTION)
@CompoundIndex(name = "userUuid_1_deviceId_1_tenantId_1", unique = true,
        def = "{'userUuid': 1, 'deviceId': 1, 'tenantId': 1}")
@Data
@Builder
public class RefreshToken {
    public static final String COLLECTION = "refreshTokens";

    @Id
    private String id;

    private String tenantId;
    private UUID userUuid;
    private String deviceId;
    private UUID token;

    @CreatedDate
    private Instant createdDate;

    @Indexed(expireAfter = "0s")
    private Instant expiresAt;
}
