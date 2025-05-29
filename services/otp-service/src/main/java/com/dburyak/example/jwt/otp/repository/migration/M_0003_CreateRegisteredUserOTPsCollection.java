package com.dburyak.example.jwt.otp.repository.migration;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "0003-create-registered-user-otps-collection", order = "0003", author = "dmytro.buryak")
public class M_0003_CreateRegisteredUserOTPsCollection {
    static final String COLLECTION_REGISTERED_USER_OTPS = "registeredUserOtps";

    @BeforeExecution
    public void executeBeforeWithoutTx(MongoDatabase mongo) {
        mongo.createCollection(COLLECTION_REGISTERED_USER_OTPS);
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongo) {
        mongo.getCollection(COLLECTION_REGISTERED_USER_OTPS).drop();
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
