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

@ChangeUnit(id = "0003-insert-super-admin-user", order = "0003", author = "dmytro.buryak")
public class M_0003_InsertSuperAdminUser {
    static final UUID SA_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final UUID SA_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final String SA_DISPLAY_NAME = "Super Admin with full access to all tenants";
    static final String SA_EMAIL = "change.me@jwt.example.dburyak.com";
    static final String SA_PROFILE_ICON = "super-admin";
    static final UUID USER_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    static final String FIELD_DISPLAY_NAME = "displayName";
    static final String FIELD_PROFILE_ICON = "profileIcon";
    static final String FIELD_CREATED_BY = "createdBy";
    static final String FIELD_CREATED_DATE = "createdDate";
    static final String FIELD_LAST_MODIFIED_BY = "lastModifiedBy";
    static final String FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = Instant.now();
        collection.insertOne(new Document(Map.of(
                FIELD_TENANT_UUID, SA_TENANT_UUID,
                FIELD_UUID, SA_USER_UUID,
                FIELD_DISPLAY_NAME, SA_DISPLAY_NAME,
                FIELD_CONTACT_INFO_EMAIL, SA_EMAIL,
                FIELD_PROFILE_ICON, SA_PROFILE_ICON,
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
                FIELD_TENANT_UUID, SA_TENANT_UUID,
                FIELD_UUID, SA_USER_UUID
        )));
    }
}
