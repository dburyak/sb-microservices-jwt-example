package com.dburyak.example.jwt.auth.repository.migration;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.util.Map;

import static com.dburyak.example.jwt.auth.repository.migration.M_0004_CreateRefreshTokensCollection.COLLECTION_REFRESH_TOKENS;
import static java.util.concurrent.TimeUnit.SECONDS;

@ChangeUnit(id = "0005-create-refreshToken-indexes", order = "0005", author = "dmytro.buryak")
public class M_0005_CreateRefreshTokenIndexes {
    static final String IDX_USER_UUID_1_DEVICE_ID_1_TENANT_UUID_1 = "userUuid_1_deviceId_1_tenantUuid_1";
    static final String IDX_EXPIRES_AT_1 = "expiresAt_1";

    static final String FIELD_TENANT_UUID = "tenantUuid";
    static final String FIELD_USER_UUID = "userUuid";
    static final String FIELD_DEVICE_ID = "deviceId";
    static final String FIELD_EXPIRES_AT = "expiresAt";

    @BeforeExecution
    public void executeBeforeWithoutTx(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_REFRESH_TOKENS);
        collection.createIndex(new Document(Map.of(FIELD_USER_UUID, 1, FIELD_DEVICE_ID, 1, FIELD_TENANT_UUID, 1)),
                new IndexOptions().name(IDX_USER_UUID_1_DEVICE_ID_1_TENANT_UUID_1).unique(true));
        // expire documents automatically after 0 seconds (right away) at "expiresAt" field time value
        collection.createIndex(new Document(Map.of(FIELD_EXPIRES_AT, 1)),
                new IndexOptions().name(IDX_EXPIRES_AT_1).expireAfter(0L, SECONDS));
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_REFRESH_TOKENS);
        collection.dropIndex(IDX_USER_UUID_1_DEVICE_ID_1_TENANT_UUID_1);
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
