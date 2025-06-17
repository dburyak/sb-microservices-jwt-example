package com.dburyak.example.jwt.tenant.repository;

import com.dburyak.example.jwt.tenant.domain.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.UUID;

import static com.dburyak.example.jwt.lib.mongo.MongoEntity.FIELD_UUID;
import static com.dburyak.example.jwt.tenant.domain.Tenant.COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class TenantRepositoryCustomImpl implements TenantRepositoryCustom {
    private static final String FIELD_NAME = "name";

    private final MongoTemplate mongo;

    @Override
    public UUID deleteByName(String name) {
        var q = query(where(FIELD_NAME).is(name));
        q.fields().include(FIELD_UUID);
        var tenant = mongo.findAndRemove(q, Tenant.class, COLLECTION);
        return tenant != null ? tenant.getUuid() : null;
    }
}
