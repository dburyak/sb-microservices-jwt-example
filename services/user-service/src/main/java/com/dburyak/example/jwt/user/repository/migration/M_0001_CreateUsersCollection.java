package com.dburyak.example.jwt.user.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "0001-create-users-collection", order = "0001", author = "dmytro.buryak")
public class M_0001_CreateUsersCollection {
    static final String COLLECTION_USERS = "users";

    @BeforeExecution
    public void executeBeforeWithoutTx(MongoDatabase mongo) {
        mongo.createCollection(COLLECTION_USERS);
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongo) {
        mongo.getCollection(COLLECTION_USERS).drop();
    }

    @Execution
    public void execute() {
        // nothing to do in TX
    }

    @RollbackExecution
    public void rollback() {
        // nothing to rollback in TX
    }
}
