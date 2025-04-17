package com.dburyak.example.jwt.api.internal.tenant.cfg;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "msg.tenant")
@Validated
@Value
@NonFinal
public class TenantMsgProperties {
    String consumerGroup;
    Topics topics;

    @ConstructorBinding
    public TenantMsgProperties(
            String consumerGroup,
            @DefaultValue Topics topics) {
        this.consumerGroup = consumerGroup;
        this.topics = topics;
    }

    @Value
    @NonFinal
    public static class Topics {
        TenantCreatedTopic tenantCreated;
        TenantDeletedTopic tenantDeleted;

        @ConstructorBinding
        public Topics(
                @DefaultValue TenantCreatedTopic tenantCreated,
                @DefaultValue TenantDeletedTopic tenantDeleted) {
            this.tenantCreated = tenantCreated;
            this.tenantDeleted = tenantDeleted;
        }

        @Value
        @NonFinal
        @EqualsAndHashCode(callSuper = true)
        public static class TenantCreatedTopic extends Topic {

            @ConstructorBinding
            public TenantCreatedTopic(
                    @DefaultValue("tenant.created") @NotBlank String name,
                    String consumerGroup,
                    @DefaultValue("redis") Transport transport) {
                super(name, consumerGroup, transport);
            }
        }

        @Value
        @NonFinal
        @EqualsAndHashCode(callSuper = true)
        public static class TenantDeletedTopic extends Topic {

            @ConstructorBinding
            public TenantDeletedTopic(
                    @DefaultValue("tenant.deleted") @NotBlank String name,
                    String consumerGroup,
                    @DefaultValue("redis") Transport transport) {
                super(name, consumerGroup, transport);
            }
        }
    }

    @Data
    public abstract static class Topic {
        protected final String name;
        protected final String consumerGroup;
        protected final Transport transport;
    }

    public enum Transport {
        REDIS;
    }
}
