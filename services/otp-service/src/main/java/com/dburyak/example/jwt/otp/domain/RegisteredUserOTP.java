package com.dburyak.example.jwt.otp.domain;

import com.dburyak.example.jwt.lib.mongo.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

import static com.dburyak.example.jwt.otp.domain.RegisteredUserOTP.COLLECTION;

/**
 * OTP for registered users identified by their {@code userUuid}.
 */
@Document(collection = COLLECTION)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegisteredUserOTP extends MongoEntity {
    public static final String COLLECTION = "registeredUserOtps";

    private UUID userUuid;
    private String deviceId;
    private Type type;
    private String code;
    private Instant expiresAt;

    public enum Type {
        /**
         * OTP for resetting password in case when the user is logged in (change password flow).
         */
        CHANGE_PASSWORD
    }
}
