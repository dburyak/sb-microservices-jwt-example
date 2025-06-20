package com.dburyak.example.jwt.user.domain;

import com.dburyak.example.jwt.lib.mongo.ExternalId;
import com.dburyak.example.jwt.lib.mongo.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.dburyak.example.jwt.user.domain.User.COLLECTION;

@Document(collection = COLLECTION)
@CompoundIndex(name = "uuid_1_tenantUuid_1", unique = true, def = "{'uuid': 1, 'tenantUuid': 1}")
@CompoundIndex(name = "email_1_tenantUuid_1", unique = true, def = "{'email': 1, 'tenantUuid': 1}")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends MongoEntity {
    public static final String COLLECTION = "users";

    private ExternalId externalId;
    private String username;
    private String displayName;
    private String profileIcon;
    private ContactInfo contactInfo;
}
