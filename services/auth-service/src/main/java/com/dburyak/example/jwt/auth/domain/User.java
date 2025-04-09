package com.dburyak.example.jwt.auth.domain;

import com.dburyak.example.jwt.lib.mongo.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

import static com.dburyak.example.jwt.auth.domain.User.COLLECTION;

@Document(collection = COLLECTION)
@Data
@SuperBuilder
@NoArgsConstructor
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
