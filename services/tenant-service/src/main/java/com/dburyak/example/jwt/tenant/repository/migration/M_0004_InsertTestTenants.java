package com.dburyak.example.jwt.tenant.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static com.dburyak.example.jwt.tenant.repository.migration.M_0001_CreateTenantsCollection.COLLECTION_TENANTS;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0002_CreateTenantIndexes.FIELD_NAME;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0002_CreateTenantIndexes.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0002_CreateTenantIndexes.FIELD_UUID;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0003_InsertSystemTenants.FIELD_CONTACT_EMAILS;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0003_InsertSystemTenants.FIELD_CREATED_BY;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0003_InsertSystemTenants.FIELD_CREATED_DATE;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0003_InsertSystemTenants.FIELD_DESCRIPTION;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0003_InsertSystemTenants.FIELD_LAST_MODIFIED_BY;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0003_InsertSystemTenants.FIELD_LAST_MODIFIED_DATE;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0003_InsertSystemTenants.TENANT_SERVICE_USER_UUID;

@ChangeUnit(id = "0004-insert-test-tenants", order = "0004", author = "dmytro.buryak")
public class M_0004_InsertTestTenants {
    static final UUID TEST_TENANT_UUID = UUID.fromString("1f3af2c3-a4f7-43c4-9df0-b877bfe7d92d");
    static final String TEST_TENANT_NAME = "test";
    static final String TEST_TENANT_DESCRIPTION = "Tenant exclusively for tests";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_TENANTS);
        var now = Instant.now();
        collection.insertOne(new Document(Map.of(
                FIELD_TENANT_UUID, TEST_TENANT_UUID,
                FIELD_UUID, TEST_TENANT_UUID,
                FIELD_NAME, TEST_TENANT_NAME,
                FIELD_DESCRIPTION, TEST_TENANT_DESCRIPTION,
                FIELD_CONTACT_EMAILS, Map.of(),
                FIELD_CREATED_BY, TENANT_SERVICE_USER_UUID,
                FIELD_CREATED_DATE, now,
                FIELD_LAST_MODIFIED_BY, TENANT_SERVICE_USER_UUID,
                FIELD_LAST_MODIFIED_DATE, now
        )));
    }

    @RollbackExecution
    public void rollback(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_TENANTS);
        collection.deleteOne(new Document(Map.of(
                FIELD_NAME, TEST_TENANT_NAME
        )));
    }
}
