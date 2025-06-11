package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.User;
import com.dburyak.example.jwt.lib.mongo.ExternalId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import java.util.UUID;

import static com.dburyak.example.jwt.lib.mongo.MongoEntity.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.lib.mongo.MongoEntity.FIELD_UUID;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private static final String FIELD_EXTERNAL_ID_EMAIL = "externalId." + ExternalId.FIELD_EMAIL;
    private static final String FIELD_PASSWORD = "password";

    private final MongoTemplate mongo;

    @Override
    public boolean updatePasswordByExternalIdEmail(UUID tenantUuid, String email, String newPassword) {
        var q = query(where(FIELD_TENANT_UUID).is(tenantUuid)
                .and(FIELD_EXTERNAL_ID_EMAIL).is(email));
        var upd = new Update().set(FIELD_PASSWORD, newPassword);
        var updRes = mongo.updateFirst(q, upd, User.class);
        return updRes.getModifiedCount() > 0;
    }

    @Override
    public boolean updatePasswordByUuid(UUID tenantUuid, UUID uuid, String newPassword) {
        var q = query(where(FIELD_TENANT_UUID).is(tenantUuid)
                .and(FIELD_UUID).is(uuid));
        var upd = new Update().set(FIELD_PASSWORD, newPassword);
        var updRes = mongo.updateFirst(q, upd, User.class);
        return updRes.getModifiedCount() > 0;
    }
}
