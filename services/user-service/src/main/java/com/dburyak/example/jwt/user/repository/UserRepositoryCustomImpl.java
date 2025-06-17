package com.dburyak.example.jwt.user.repository;

import com.dburyak.example.jwt.user.domain.ContactInfo;
import com.dburyak.example.jwt.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.UUID;

import static com.dburyak.example.jwt.lib.mongo.MongoEntity.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.lib.mongo.MongoEntity.FIELD_UUID;
import static com.dburyak.example.jwt.user.domain.User.COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private static final String FIELD_CONTACT_INFO = "contactInfo";
    private final MongoTemplate mongo;

    @Override
    public ContactInfo findContactInfoByTenantUuidAndUuid(UUID tenantUuid, UUID uuid) {
        var q = query(where(FIELD_TENANT_UUID).is(tenantUuid)
                .and(FIELD_UUID).is(uuid));
        q.fields().include(FIELD_CONTACT_INFO);
        var user = mongo.findOne(q, User.class, COLLECTION);
        return (user != null) ? user.getContactInfo() : null;
    }
}
