package com.dburyak.example.jwt.otp.repository.migration;

import com.mongodb.client.model.IndexOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.util.Map;

import static com.dburyak.example.jwt.otp.repository.migration.M_0001_CreateExternallyIdentifiedOTPsCollection.COLLECTION_EXTERNALLY_IDENTIFIED_OTPS;
import static java.util.concurrent.TimeUnit.SECONDS;

@ChangeUnit(id = "0004-create-registered-user-otps-indexes", order = "0004", author = "dmytro.buryak")
public class M_0004_CreateRegisteredUserOTPsIndexes {
    static final String IDX_USER_UUID_1_DEVICE_ID_1_TYPE_1_TENANT_UUID_1 = "userUuid_1_deviceId_1_type_1_tenantUuid_1";
    /// Special idx for automatic cleanup of expired otps on mongodb side.
    static final String IDX_EXPIRES_AT_1 = "expiresAt_1";

    static final String FIELD_USER_UUID = "userUuid";
    static final String FIELD_DEVICE_ID = "deviceId";
    static final String FIELD_TYPE = "type";
    static final String FIELD_TENANT_UUID = "tenantUuid";
    static final String FIELD_EXPIRES_AT = "expiresAt";

    @BeforeExecution
    public void executeBeforeWithoutTx(com.mongodb.client.MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_EXTERNALLY_IDENTIFIED_OTPS);
        collection.createIndex(new Document(Map.of(
                        FIELD_USER_UUID, 1,
                        FIELD_DEVICE_ID, 1,
                        FIELD_TYPE, 1,
                        FIELD_TENANT_UUID, 1)),
                new IndexOptions().name(IDX_USER_UUID_1_DEVICE_ID_1_TYPE_1_TENANT_UUID_1).unique(true));
        collection.createIndex(new Document(Map.of(
                        FIELD_EXPIRES_AT, 1)),
                new IndexOptions().name(IDX_EXPIRES_AT_1).expireAfter(0L, SECONDS));
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(com.mongodb.client.MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_EXTERNALLY_IDENTIFIED_OTPS);
        collection.dropIndex(IDX_USER_UUID_1_DEVICE_ID_1_TYPE_1_TENANT_UUID_1);
        collection.dropIndex(IDX_EXPIRES_AT_1);
    }

    @Execution
    public void execute() {
        // nothing to do in TX
    }

    @RollbackExecution
    public void rollback() {
        // nothing to rollback in TX
    }
}
