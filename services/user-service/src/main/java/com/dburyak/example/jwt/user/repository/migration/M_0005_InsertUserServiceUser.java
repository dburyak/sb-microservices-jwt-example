package com.dburyak.example.jwt.user.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.time.Instant;
import java.util.Map;

import static com.dburyak.example.jwt.user.repository.migration.M_0001_CreateUsersCollection.COLLECTION_USERS;
import static com.dburyak.example.jwt.user.repository.migration.M_0002_CreateUserIndexes.FIELD_EMAIL;
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

@ChangeUnit(id = "0005-insert-user-service-user", order = "0005", author = "dmytro.buryak")
public class M_0005_InsertUserServiceUser {
    static final String DISPLAY_NAME = "User Service User";
    static final String EMAIL = "user.service@jwt.example.dburyak.com";
    static final String PROFILE_ICON = "service";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = Instant.now();
        collection.insertOne(new Document(Map.of(
                FIELD_TENANT_UUID, SERVICE_TENANT_UUID,
                FIELD_UUID, USER_SERVICE_USER_UUID,
                FIELD_DISPLAY_NAME, DISPLAY_NAME,
                FIELD_EMAIL, EMAIL,
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
                FIELD_UUID, USER_SERVICE_USER_UUID
        )));
    }
}
