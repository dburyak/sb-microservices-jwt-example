package com.dburyak.example.jwt.auth.err;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.lib.err.NotFoundException;

public class ExternallyIdentifiedUserNotFoundException extends NotFoundException {
    public ExternallyIdentifiedUserNotFoundException(ExternalId externalId) {
        super("User(externalId=%s)".formatted(externalId));
    }
}
