package com.dburyak.example.jwt.auth.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

import static com.dburyak.example.jwt.auth.domain.RefreshToken.COLLECTION;

@Document(collection = COLLECTION)
@Data
@Builder
public class RefreshToken {
    public static final String COLLECTION = "refreshTokens";

    @Id
    private String id;

    private UUID tenantUuid;
    private UUID userUuid;
    private String deviceId;
    private UUID token;

    @CreatedDate
    private Instant createdDate;
    private Instant expiresAt;
}
