package com.dburyak.example.jwt.api.common;

import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AuditedModel {

    @JsonView({READ.class})
    protected final UUID createdBy;

    @JsonView({READ.class})
    protected final Instant createdDate;

    @JsonView({READ.class})
    protected final UUID lastModifiedBy;

    @JsonView({READ.class})
    protected final Instant lastModifiedDate;
}
