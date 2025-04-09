package com.dburyak.example.jwt.tenant.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.dburyak.example.jwt.tenant.repository.migration.M_0001_CreateTenantsCollection.COLLECTION_TENANTS;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0002_CreateTenantIndexes.FIELD_NAME;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0002_CreateTenantIndexes.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.tenant.repository.migration.M_0002_CreateTenantIndexes.FIELD_UUID;

@ChangeUnit(id = "0003-insert-system-tenants", order = "0003", author = "dmytro.buryak")
public class M_0003_InsertSystemTenants {
    static final String SERVICE_TENANT_NAME = "service";
    static final UUID SERVICE_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final String SA_TENANT_NAME = "system-admin";
    static final UUID SA_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    static final UUID TENANT_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    static final String FIELD_DESCRIPTION = "description";
    static final String FIELD_CONTACT_EMAILS = "contactEmails";
    static final String FIELD_CREATED_BY = "createdBy";
    static final String FIELD_CREATED_DATE = "createdDate";
    static final String FIELD_LAST_MODIFIED_BY = "lastModifiedBy";
    static final String FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_TENANTS);
        var now = Instant.now();
        List.of(
                List.of(SERVICE_TENANT_NAME, SERVICE_TENANT_UUID, "System tenant for services"),
                List.of(SA_TENANT_NAME, SA_TENANT_UUID, "System tenant for super-admin users")
        ).forEach(tuple -> {
            var tenantName = (String) tuple.get(0);
            var tenantUuid = (UUID) tuple.get(1);
            var tenantDescription = (String) tuple.get(2);
            collection.insertOne(new Document(Map.of(
                    FIELD_TENANT_UUID, tenantUuid,
                    FIELD_UUID, tenantUuid,
                    FIELD_NAME, tenantName,
                    FIELD_DESCRIPTION, tenantDescription,
                    FIELD_CONTACT_EMAILS, Map.of(),
                    FIELD_CREATED_BY, TENANT_SERVICE_USER_UUID,
                    FIELD_CREATED_DATE, now,
                    FIELD_LAST_MODIFIED_BY, TENANT_SERVICE_USER_UUID,
                    FIELD_LAST_MODIFIED_DATE, now
            )));
        });
    }

    @RollbackExecution
    public void rollback(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_TENANTS);
        List.of(SERVICE_TENANT_UUID, SA_TENANT_UUID).forEach(tenantUuid ->
                collection.deleteOne(new Document(Map.of(FIELD_UUID, tenantUuid))));
    }
}
