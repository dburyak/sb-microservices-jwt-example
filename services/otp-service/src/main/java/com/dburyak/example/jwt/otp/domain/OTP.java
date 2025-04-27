package com.dburyak.example.jwt.otp.domain;

import com.dburyak.example.jwt.lib.mongo.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

import static com.dburyak.example.jwt.otp.domain.OTP.COLLECTION;

@Document(collection = COLLECTION)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OTP extends MongoEntity {
    public static final String COLLECTION = "otps";

    private UUID userUuid;
    private String deviceId;
    private OTPType type;
    private String code;
    private Instant expiresAt;
}
