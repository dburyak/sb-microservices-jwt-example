package com.dburyak.example.jwt.auth.repository;

import com.dburyak.example.jwt.auth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;

import static com.dburyak.example.jwt.auth.domain.RefreshToken.COLLECTION;
import static org.springframework.data.mongodb.core.ReplaceOptions.replaceOptions;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class RefreshTokenRepositoryCustomImpl implements RefreshTokenRepositoryCustom {
    private static final String TENANT_ID = "tenantId";
    private static final String USER_UUID = "userUuid";
    private static final String DEVICE_ID = "deviceId";
    private static final String TOKEN = "token";
    private static final String EXPIRES_AT = "expiresAt";
    private final MongoTemplate mongo;

    @Override
    public void insertOrReplaceByTenantIdAndUserUuidAndDeviceId(RefreshToken refreshToken) {
        var q = query(where(TENANT_ID).is(refreshToken.getTenantId())
                .and(USER_UUID).is(refreshToken.getUserUuid())
                .and(DEVICE_ID).is(refreshToken.getDeviceId()));
        var opts = replaceOptions().upsert();
        mongo.replace(q, refreshToken, opts, COLLECTION);
    }

    @Override
    public boolean replaceOneByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(RefreshToken refreshToken) {
        var q = query(where(TENANT_ID).is(refreshToken.getTenantId())
                .and(USER_UUID).is(refreshToken.getUserUuid())
                .and(DEVICE_ID).is(refreshToken.getDeviceId())
                .and(TOKEN).is(refreshToken.getToken())
                .and(EXPIRES_AT).gt(Instant.now()));
        var replacedToken = mongo.replace(q, refreshToken, COLLECTION);
        return replacedToken != null;
    }

    @Override
    public boolean deleteByTenantIdAndUserUuidAndDeviceIdAndTokenAndNotExpired(RefreshToken refreshToken) {
        var q = query(where(TENANT_ID).is(refreshToken.getTenantId())
                .and(USER_UUID).is(refreshToken.getUserUuid())
                .and(DEVICE_ID).is(refreshToken.getDeviceId())
                .and(TOKEN).is(refreshToken.getToken())
                .and(EXPIRES_AT).gt(Instant.now()));
        var delResult = mongo.remove(q, COLLECTION);
        return delResult.getDeletedCount() > 0;
    }
}
