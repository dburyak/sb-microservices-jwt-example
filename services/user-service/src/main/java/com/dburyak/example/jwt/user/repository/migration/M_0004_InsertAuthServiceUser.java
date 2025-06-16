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
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_UUID;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_CONTACT_INFO_EMAIL;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_CREATED_BY;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_CREATED_DATE;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_DISPLAY_NAME;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_LAST_MODIFIED_BY;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_LAST_MODIFIED_DATE;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.FIELD_PROFILE_ICON;
import static com.dburyak.example.jwt.user.repository.migration.M_0003_InsertSuperAdminUser.USER_SERVICE_USER_UUID;

@ChangeUnit(id = "0004-insert-auth-service-user", order = "0004", author = "dmytro.buryak")
public class M_0004_InsertAuthServiceUser {
    static final UUID SERVICE_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    static final UUID AUTH_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    static final String DISPLAY_NAME = "auth-service user";
    static final String EMAIL = "auth.service@jwt.example.dburyak.com";
    static final String PROFILE_ICON = "service";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = Instant.now();
        collection.insertOne(new Document(Map.of(
                FIELD_TENANT_UUID, SERVICE_TENANT_UUID,
                FIELD_UUID, AUTH_SERVICE_USER_UUID,
                FIELD_EXTERNAL_ID_EMAIL, EMAIL,
                FIELD_DISPLAY_NAME, DISPLAY_NAME,
                FIELD_PROFILE_ICON, PROFILE_ICON,
                FIELD_CONTACT_INFO_EMAIL, EMAIL,
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
                FIELD_UUID, AUTH_SERVICE_USER_UUID
        )));
    }
}
