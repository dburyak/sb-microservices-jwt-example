package com.dburyak.example.jwt.tenant.domain;

import com.dburyak.example.jwt.lib.mongo.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

import static com.dburyak.example.jwt.tenant.domain.Tenant.COLLECTION;

@Document(collection = COLLECTION)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tenant extends MongoEntity {
    public static final String COLLECTION = "tenants";

    private String name;
    private String description;
    private Set<String> contactEmails;
}
