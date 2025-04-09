package com.dburyak.example.jwt.tenant.repository.migration;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.util.Map;

import static com.dburyak.example.jwt.tenant.repository.migration.M_0001_CreateTenantsCollection.COLLECTION_TENANTS;

@ChangeUnit(id = "0002-create-tenant-indexes", order = "0002", author = "dmytro.buryak")
public class M_0002_CreateTenantIndexes {
    static final String IDX_UUID_1 = "uuid_1";
    static final String IDX_TENANT_UUID_1 = "tenantUuid_1";
    static final String IDX_NAME_1 = "name_1";

    static final String FIELD_UUID = "uuid";
    static final String FIELD_TENANT_UUID = "tenantUuid";
    static final String FIELD_NAME = "name";

    @BeforeExecution
    public void executeBeforeWithoutTx(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_TENANTS);
        collection.createIndex(new Document(Map.of(FIELD_UUID, 1)),
                new IndexOptions().name(IDX_UUID_1).unique(true));
        collection.createIndex(new Document(Map.of(FIELD_TENANT_UUID, 1)),
                new IndexOptions().name(IDX_TENANT_UUID_1).unique(true));
        collection.createIndex(new Document(Map.of(FIELD_NAME, 1)),
                new IndexOptions().name(IDX_NAME_1).unique(true));
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_TENANTS);
        collection.dropIndex(IDX_UUID_1);
        collection.dropIndex(IDX_TENANT_UUID_1);
        collection.dropIndex(IDX_NAME_1);
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
