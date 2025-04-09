package com.dburyak.example.jwt.auth.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "0004-create-refreshTokens-collection", order = "0004", author = "dmytro.buryak")
public class M_0004_CreateRefreshTokensCollection {
    static final String COLLECTION_REFRESH_TOKENS = "refreshTokens";

    @BeforeExecution
    public void executeBeforeWithoutTx(MongoDatabase mongo) {
        mongo.createCollection(COLLECTION_REFRESH_TOKENS);
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongo) {
        mongo.getCollection(COLLECTION_REFRESH_TOKENS).drop();
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
