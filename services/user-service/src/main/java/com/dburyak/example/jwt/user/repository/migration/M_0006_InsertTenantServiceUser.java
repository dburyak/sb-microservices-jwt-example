package com.dburyak.example.jwt.user.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static com.dburyak.example.jwt.user.repository.migration.M_0001_CreateUsersCollection.COLLECTION_USERS;
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_CONTACT_INFO_EMAIL;
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_UUID;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_CREATED_BY;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_CREATED_DATE;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_DISPLAY_NAME;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_LAST_MODIFIED_BY;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_LAST_MODIFIED_DATE;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_PROFILE_ICON;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.USER_SERVICE_USER_UUID;
import static com.dburyak.example.jwt.user.repository.migration.M_0004_InsertAuthServiceUser.SERVICE_TENANT_UUID;

@ChangeUnit(id = "0006-insert-tenant-service-user", order = "0006", author = "dmytro.buryak")
public class M_0006_InsertTenantServiceUser {
    static final UUID TENANT_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    static final String DISPLAY_NAME = "tenant-service user";
    static final String EMAIL = "tenant.service@jwt.example.dbruayk.com";
    static final String PROFILE_ICON = "service";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = Instant.now();
        collection.insertOne(new Document(Map.of(
                FIELD_TENANT_UUID, SERVICE_TENANT_UUID,
                FIELD_UUID, TENANT_SERVICE_USER_UUID,
                FIELD_DISPLAY_NAME, DISPLAY_NAME,
                FIELD_CONTACT_INFO_EMAIL, EMAIL,
                FIELD_PROFILE_ICON, PROFILE_ICON,
                FIELD_CREATED_BY, USER_SERVICE_USER_UUID,
                FIELD_CREATED_DATE, now,
                FIELD_LAST_MODIFIED_BY, USER_SERVICE_USER_UUID,
                FIELD_LAST_MODIFIED_DATE, now
        )));
    }

    @RollbackExecution
    public void rollback(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        collection.deleteOne(new Document(Map.of(
                FIELD_TENANT_UUID, SERVICE_TENANT_UUID,
                FIELD_UUID, TENANT_SERVICE_USER_UUID
        )));
    }
}
