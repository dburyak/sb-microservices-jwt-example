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
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_EXTERNAL_ID_EMAIL;
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_USERNAME;
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_UUID;
import static java.util.Map.entry;

@ChangeUnit(id = "0003-insert-super-admin-user", order = "0003", author = "dmytro.buryak")
public class M_0003_InsertSuperAdminUser {
    static final UUID SA_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final UUID SA_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final String SA_DISPLAY_NAME = "Super Admin with full access to all tenants";
    static final String SA_EMAIL = "super.admin@jwt.example.dburyak.com";
    static final String SA_USERNAME = "sa";
    static final String SA_PROFILE_ICON = "super-admin";
    static final UUID USER_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    static final String FIELD_DISPLAY_NAME = "displayName";
    static final String FIELD_PROFILE_ICON = "profileIcon";
    static final String FIELD_CONTACT_INFO = "contactInfo";
    static final String FIELD_CONTACT_INFO_EMAIL = FIELD_CONTACT_INFO + ".email";
    static final String FIELD_CREATED_BY = "createdBy";
    static final String FIELD_CREATED_DATE = "createdDate";
    static final String FIELD_LAST_MODIFIED_BY = "lastModifiedBy";
    static final String FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = Instant.now();
        collection.insertOne(new Document(Map.ofEntries(
                entry(FIELD_TENANT_UUID, SA_TENANT_UUID),
                entry(FIELD_UUID, SA_USER_UUID),
                entry(FIELD_EXTERNAL_ID_EMAIL, SA_EMAIL),
                entry(FIELD_USERNAME, SA_USERNAME),
                entry(FIELD_DISPLAY_NAME, SA_DISPLAY_NAME),
                entry(FIELD_PROFILE_ICON, SA_PROFILE_ICON),
                entry(FIELD_CONTACT_INFO_EMAIL, SA_EMAIL),
                entry(FIELD_CREATED_BY, USER_SERVICE_USER_UUID),
                entry(FIELD_CREATED_DATE, now),
                entry(FIELD_LAST_MODIFIED_BY, USER_SERVICE_USER_UUID),
                entry(FIELD_LAST_MODIFIED_DATE, now)
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
