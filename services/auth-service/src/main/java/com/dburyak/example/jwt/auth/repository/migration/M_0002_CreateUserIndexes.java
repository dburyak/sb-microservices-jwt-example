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

import static com.dburyak.example.jwt.auth.repository.migration.M_0001_CreateUsersCollection.COLLECTION_USERS;

@ChangeUnit(id = "0002-create-user-indexes", order = "0002", author = "dmytro.buryak")
public class M_0002_CreateUserIndexes {
    static final String IDX_UUID_1_TENANT_UUID_1 = "uuid_1_tenantUuid_1";
    static final String IDX_USERNAME_1_TENANT_ID_1 = "username_1_tenantUuid_1";
    static final String IDX_EXTERNAL_ID_EMAIL_1_TENANT_ID_1 = "externalId_email_1_tenantUuid_1";

    static final String FIELD_UUID = "uuid";
    static final String FIELD_TENANT_UUID = "tenantUuid";
    static final String FIELD_USERNAME = "username";
    static final String FIELD_EXTERNAL_ID = "externalId";
    static final String FIELD_EMAIL = "email";
    static final String FIELD_EXTERNAL_ID_EMAIL = FIELD_EXTERNAL_ID + "." + FIELD_EMAIL;

    @BeforeExecution
    public void executeBeforeWithoutTx(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        collection.createIndex(new Document(Map.of(FIELD_UUID, 1, FIELD_TENANT_UUID, 1)),
                new IndexOptions().name(IDX_UUID_1_TENANT_UUID_1).unique(true));
        collection.createIndex(new Document(Map.of(FIELD_USERNAME, 1, FIELD_TENANT_UUID, 1)),
                new IndexOptions().name(IDX_USERNAME_1_TENANT_ID_1).unique(true));
        collection.createIndex(new Document(Map.of(FIELD_EXTERNAL_ID_EMAIL, 1, FIELD_TENANT_UUID, 1)),
                new IndexOptions()
                        .name(IDX_EXTERNAL_ID_EMAIL_1_TENANT_ID_1)
                        .unique(true)
                        .partialFilterExpression(new Document(Map.of(
                                FIELD_EXTERNAL_ID_EMAIL, new Document("$exists", true)
                        ))));
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        collection.dropIndex(IDX_UUID_1_TENANT_UUID_1);
        collection.dropIndex(IDX_USERNAME_1_TENANT_ID_1);
        collection.dropIndex(IDX_EXTERNAL_ID_EMAIL_1_TENANT_ID_1);
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
