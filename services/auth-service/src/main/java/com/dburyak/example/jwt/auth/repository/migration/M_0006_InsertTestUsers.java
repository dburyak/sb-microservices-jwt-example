package com.dburyak.example.jwt.auth.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import org.bson.Document;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.auth.repository.migration.M_0001_CreateUsersCollection.COLLECTION_USERS;
import static com.dburyak.example.jwt.auth.repository.migration.M_0002_CreateUserIndexes.FIELD_TENANT_UUID;
import static com.dburyak.example.jwt.auth.repository.migration.M_0002_CreateUserIndexes.FIELD_USERNAME;
import static com.dburyak.example.jwt.auth.repository.migration.M_0002_CreateUserIndexes.FIELD_UUID;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.AUTH_SERVICE_USER_UUID;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.FIELD_CREATED_BY;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.FIELD_CREATED_DATE;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.FIELD_LAST_MODIFIED_BY;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.FIELD_LAST_MODIFIED_DATE;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.FIELD_PASSWORD;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.FIELD_ROLES;
import static com.dburyak.example.jwt.auth.repository.migration.M_0003_InsertSystemUsers.ROLE_SA;

@ChangeUnit(id = "0006-insert-test-users", order = "0006", author = "dmytro.buryak")
public class M_0006_InsertTestUsers {
    static final UUID TEST_TENANT_UUID = UUID.fromString("1f3af2c3-a4f7-43c4-9df0-b877bfe7d92d");
    static final String TEST_SA_USERNAME = "test-super-admin";
    static final UUID TEST_SA_UUID = UUID.fromString("8d61c52b-e075-4146-b5ad-78baafcf70b0");

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = System.currentTimeMillis();

        LinkedHashMap<String, Object> userMap = new LinkedHashMap<>();
        userMap.put(FIELD_TENANT_UUID, TEST_TENANT_UUID);
        userMap.put(FIELD_UUID, TEST_SA_UUID);
        userMap.put(FIELD_USERNAME, TEST_SA_USERNAME);
        userMap.put(FIELD_PASSWORD, null); // no initial password test SA user
        userMap.put(FIELD_ROLES, Set.of(ROLE_SA));
        userMap.put(FIELD_CREATED_BY, AUTH_SERVICE_USER_UUID);
        userMap.put(FIELD_CREATED_DATE, now);
        userMap.put(FIELD_LAST_MODIFIED_BY, AUTH_SERVICE_USER_UUID);
        userMap.put(FIELD_LAST_MODIFIED_DATE, now);

        collection.insertOne(new Document(userMap));
    }
}
