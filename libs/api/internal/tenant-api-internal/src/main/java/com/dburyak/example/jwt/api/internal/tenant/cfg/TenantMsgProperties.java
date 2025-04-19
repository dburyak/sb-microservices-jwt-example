package com.dburyak.example.jwt.api.internal.tenant.cfg;

import com.dburyak.example.jwt.api.internal.common.msg.TopicCfgProps;
import com.dburyak.example.jwt.api.internal.common.msg.Transport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
        public static class TenantCreatedTopic extends TopicCfgProps {

            @ConstructorBinding
            public TenantCreatedTopic(
                    @DefaultValue("tenant.created") @NotBlank String topicName,
                    String subscriptionName,
                    String consumerGroup,
                    @DefaultValue("redis") @NotNull Transport transport) {
                super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
            }
        }

        @Value
        @NonFinal
        @EqualsAndHashCode(callSuper = true)
        public static class TenantDeletedTopic extends TopicCfgProps {

            @ConstructorBinding
            public TenantDeletedTopic(
                    @DefaultValue("tenant.deleted") @NotBlank String topicName,
                    String subscriptionName,
                    String consumerGroup,
                    @DefaultValue("redis") @NotNull Transport transport) {
                super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
            }
        }
    }
}
