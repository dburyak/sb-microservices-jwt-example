package com.dburyak.example.jwt.auth.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.auth.repository.migration.M_0001_CreateUsersCollection.COLLECTION_USERS;
import static com.dburyak.example.jwt.auth.repository.migration.M_0002_CreateUserIndexes.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.auth.repository.migration.M_0002_CreateUserIndexes.FIELD_USERNAME;
import static com.dburyak.example.jwt.auth.repository.migration.M_0002_CreateUserIndexes.FIELD_UUID;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSuperAdminUser.AUTH_SERVICE_USER_UUID;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSuperAdminUser.FIELD_CREATED_BY;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSuperAdminUser.FIELD_CREATED_DATE;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSuperAdminUser.FIELD_LAST_MODIFIED_BY;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSuperAdminUser.FIELD_LAST_MODIFIED_DATE;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSuperAdminUser.FIELD_PASSWORD;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSuperAdminUser.FIELD_ROLES;
import static com.dburyak.example.jwt.auth.repository.migration.M_0006_InsertAuthServiceUser.ROLE_SERVICE;
import static com.dburyak.example.jwt.auth.repository.migration.M_0006_InsertAuthServiceUser.SERVICE_TENANT_UUID;

@ChangeUnit(id = "0007-insert-user-service-user", order = "0007", author = "dmytro.buryak")
public class M_0007_InsertUserServiceUser {
    static final String USERNAME = "user-service";
    static final UUID USER_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = Instant.now();
        LinkedHashMap<String, Object> userMap = new LinkedHashMap<>();
        userMap.put(FIELD_TENANT_UUID, SERVICE_TENANT_UUID);
        userMap.put(FIELD_UUID, USER_SERVICE_USER_UUID);
        userMap.put(FIELD_USERNAME, USERNAME);
        userMap.put(FIELD_PASSWORD, null); // no initial password for service user
        userMap.put(FIELD_ROLES, Set.of(ROLE_SERVICE));
        userMap.put(FIELD_CREATED_BY, AUTH_SERVICE_USER_UUID);
        userMap.put(FIELD_CREATED_DATE, now);
        userMap.put(FIELD_LAST_MODIFIED_BY, AUTH_SERVICE_USER_UUID);
        userMap.put(FIELD_LAST_MODIFIED_DATE, now);
        collection.insertOne(new Document(userMap));
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
