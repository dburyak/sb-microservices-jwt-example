package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP;
import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.UUID;

import static com.dburyak.example.jwt.lib.mongo.MongoEntity.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.otp.domain.RegisteredUserOTP.COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class RegisteredUserOTPRepositoryCustomImpl implements RegisteredUserOTPRepositoryCustom {
    // field names
    private static final String FIELD_USER_UUID = "userUuid";
    private static final String FIELD_DEVICE_ID = "deviceId";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_EXPIRES_AT = "expiresAt";

    private final MongoTemplate mongo;

    @Override
    public RegisteredUserOTP insertOrReplaceByTenantUuidAndUserUuidAndDeviceIdAndType(RegisteredUserOTP otp) {
        var q = query(where(FIELD_USER_UUID).is(otp.getUserUuid())
                .and(FIELD_DEVICE_ID).is(otp.getDeviceId())
                .and(FIELD_TYPE).is(otp.getType())
                .and(FIELD_TENANT_UUID).is(otp.getTenantUuid()));
        var opts = new FindAndReplaceOptions().upsert();
        return mongo.findAndReplace(q, otp, opts, RegisteredUserOTP.class, COLLECTION);
    }

    @Override
    public RegisteredUserOTP findAndDeleteByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(
            UUID tenantUuid, UUID userUuid, String deviceId, Type type, String code, Instant expiresAtBefore) {
        var q = query(where(FIELD_USER_UUID).is(userUuid)
                .and(FIELD_DEVICE_ID).is(deviceId)
                .and(FIELD_TYPE).is(type)
                .and(FIELD_TENANT_UUID).is(tenantUuid)
                .and(FIELD_CODE).is(code)
                .and(FIELD_EXPIRES_AT).lt(expiresAtBefore));
        return mongo.findAndRemove(q, RegisteredUserOTP.class, COLLECTION);
    }
}
