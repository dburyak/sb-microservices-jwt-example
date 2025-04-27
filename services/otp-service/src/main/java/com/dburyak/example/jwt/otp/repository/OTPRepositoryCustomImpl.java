package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.OTP;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.dburyak.example.jwt.otp.domain.OTP.COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class OTPRepositoryCustomImpl implements OTPRepositoryCustom {
    private static final String TENANT_UUID = "tenantUuid";
    private static final String USER_UUID = "userUuid";
    private static final String DEVICE_ID = "deviceId";
    private static final String TYPE = "type";
    private static final String CODE = "code";
    private static final String EXPIRES_AT = "expiresAt";
    private final MongoTemplate mongo;

    @Override
    public OTP insertOrReplaceByTenantUuidAndUserUuidAndDeviceIdAndType(OTP otp) {
        var q = query(where(USER_UUID).is(otp.getUserUuid())
                .and(DEVICE_ID).is(otp.getDeviceId())
                .and(TYPE).is(otp.getType())
                .and(TENANT_UUID).is(otp.getTenantUuid()));
        var opts = new FindAndReplaceOptions().upsert();
        return mongo.findAndReplace(q, otp, opts, OTP.class, COLLECTION);
    }
}
