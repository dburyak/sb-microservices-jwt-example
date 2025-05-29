package com.dburyak.example.jwt.otp.domain;

import com.dburyak.example.jwt.lib.mongo.ExternalId;
import com.dburyak.example.jwt.lib.mongo.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

import static com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP.COLLECTION;

/**
 * OTP for users identified by external identifiers such as email or phone number. Used when the user is either
 * not registered or their UUID is not known (registered but not authenticated user).
 */
@Document(collection = COLLECTION)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExternallyIdentifiedOTP extends MongoEntity {
    public static final String COLLECTION = "externallyIdentifiedOtps";

    private ExternalId externalId; // email, phone number, etc., when userUuid is not known (anonymous user)
    private String deviceId;
    private Type type;
    private String code;
    private Instant expiresAt;

    public enum Type {
        EMAIL_REGISTRATION,

        /**
         * OTP for resetting password in case when the user is not logged in (forgot password flow).
         */
        PASSWORD_RESET
    }
}
