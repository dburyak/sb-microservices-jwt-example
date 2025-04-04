package com.dburyak.example.jwt.auth.domain;

import com.dburyak.example.jwt.lib.mongo.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

import static com.dburyak.example.jwt.auth.domain.User.COLLECTION;

@Document(collection = COLLECTION)
@CompoundIndex(name = "username_1_tenantId_1", unique = true,
        def = "{'username': 1, 'tenantId': 1}")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends MongoEntity {
    public static final String COLLECTION = "users";

    /**
     * Human friendly name that user can use to log in. Should be unique within tenant.
     */
    private String username;
    private String password;
    private Set<String> roles;
}
