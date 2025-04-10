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

@ChangeUnit(id = "0003-insert-system-users", order = "0003", author = "dmytro.buryak")
public class M_0003_InsertSystemUsers {
    static final String SA_USERNAME = "sa";
    static final String ROLE_SA = "sa";
    static final UUID SA_TENANT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final UUID SA_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    static final UUID AUTH_SERVICE_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    static final String FIELD_PASSWORD = "password";
    static final String FIELD_ROLES = "roles";
    static final String FIELD_CREATED_BY = "createdBy";
    static final String FIELD_CREATED_DATE = "createdDate";
    static final String FIELD_LAST_MODIFIED_BY = "lastModifiedBy";
    static final String FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";

    @Execution
    public void execute(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        var now = Instant.now();
        LinkedHashMap<String, Object> saUserMap = new LinkedHashMap<>();
        saUserMap.put(FIELD_TENANT_UUID, SA_TENANT_UUID);
        saUserMap.put(FIELD_UUID, SA_USER_UUID);
        saUserMap.put(FIELD_USERNAME, SA_USERNAME);
        saUserMap.put(FIELD_PASSWORD, null); // no initial password for SA
        saUserMap.put(FIELD_ROLES, Set.of(ROLE_SA));
        saUserMap.put(FIELD_CREATED_BY, AUTH_SERVICE_USER_UUID);
        saUserMap.put(FIELD_CREATED_DATE, now);
        saUserMap.put(FIELD_LAST_MODIFIED_BY, AUTH_SERVICE_USER_UUID);
        saUserMap.put(FIELD_LAST_MODIFIED_DATE, now);
        collection.insertOne(new Document(saUserMap));
    }

    @RollbackExecution
    public void rollback(MongoDatabase mongo) {
        var collection = mongo.getCollection(COLLECTION_USERS);
        collection.deleteOne(new Document(Map.of(
                FIELD_TENANT_UUID, SA_TENANT_UUID,
                FIELD_USERNAME, SA_USERNAME
        )));
    }
}
