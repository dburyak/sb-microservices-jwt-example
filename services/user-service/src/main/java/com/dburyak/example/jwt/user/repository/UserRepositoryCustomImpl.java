package com.dburyak.example.jwt.user.repository;

import com.dburyak.example.jwt.user.domain.ContactInfo;
import com.dburyak.example.jwt.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private static final String TENANT_UUID = "tenantUuid";
    private static final String UUID = "uuid";
    private static final String CONTACT_INFO = "contactInfo";
    private final MongoTemplate mongo;

    @Override
    public ContactInfo findContactInfoByTenantUuidAndUuid(UUID tenantUuid, UUID uuid) {
        var q = query(where(TENANT_UUID).is(tenantUuid)
                .and(UUID).is(uuid));
        q.fields().include(CONTACT_INFO);
        var user = mongo.findOne(q, User.class);
        return (user != null) ? user.getContactInfo() : null;
    }
}
