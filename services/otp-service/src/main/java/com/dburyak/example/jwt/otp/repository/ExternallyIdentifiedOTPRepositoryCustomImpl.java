package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.lib.mongo.ExternalId;
import com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.UUID;

import static com.dburyak.example.jwt.lib.mongo.MongoEntity.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP.COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class ExternallyIdentifiedOTPRepositoryCustomImpl implements ExternallyIdentifiedOTPRepositoryCustom {
    // field names
    private static final String FIELD_EXTERNAL_ID = "externalId";
    private static final String FIELD_EXTERNAL_ID_EMAIL = FIELD_EXTERNAL_ID + "." + ExternalId.FIELD_EMAIL;
    private static final String FIELD_DEVICE_ID = "deviceId";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_EXPIRES_AT = "expiresAt";

    private final MongoTemplate mongo;

    @Override
    public ExternallyIdentifiedOTP insertOrReplaceByTenantUuidAndEmailAndDeviceIdAndType(ExternallyIdentifiedOTP otp) {
        var q = query(where(FIELD_EXTERNAL_ID_EMAIL).is(otp.getExternalId().getEmail())
                .and(FIELD_DEVICE_ID).is(otp.getDeviceId())
                .and(FIELD_TYPE).is(otp.getType())
                .and(FIELD_TENANT_UUID).is(otp.getTenantUuid()));
        var opts = new FindAndReplaceOptions().upsert();
        return mongo.findAndReplace(q, otp, opts, ExternallyIdentifiedOTP.class, COLLECTION);
    }

    @Override
    public ExternallyIdentifiedOTP findAndDeleteByTenantUuidAndEmailAndDeviceIdAndTypeAndCodeAndExpiresAtAfter(
            UUID tenantUuid, String email, String deviceId, Type type, String code, Instant expiresAtAfter) {
        var q = query(where(FIELD_EXTERNAL_ID_EMAIL).is(email)
                .and(FIELD_DEVICE_ID).is(deviceId)
                .and(FIELD_TYPE).is(type)
                .and(FIELD_TENANT_UUID).is(tenantUuid)
                .and(FIELD_CODE).is(code)
                .and(FIELD_EXPIRES_AT).gt(expiresAtAfter));
        return mongo.findAndRemove(q, ExternallyIdentifiedOTP.class, COLLECTION);
    }
}
