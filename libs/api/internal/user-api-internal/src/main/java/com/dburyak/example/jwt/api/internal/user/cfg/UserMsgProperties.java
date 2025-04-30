package com.dburyak.example.jwt.api.internal.user.cfg;

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

@ConfigurationProperties(prefix = "msg.user")
@Validated
@Value
@NonFinal
public class UserMsgProperties {
    String consumerGroup;
    Topics topics;

    @ConstructorBinding
    public UserMsgProperties(
            String consumerGroup,
            @DefaultValue Topics topics) {
        this.consumerGroup = consumerGroup;
        this.topics = topics;
    }

    @Value
    @NonFinal
    public static class Topics {
        UserCreatedTopic userCreated;
        UserDeletedTopic userDeleted;

        public Topics(
                @DefaultValue UserCreatedTopic userCreated,
                @DefaultValue UserDeletedTopic userDeleted) {
            this.userCreated = userCreated;
            this.userDeleted = userDeleted;
        }
    }

    @Value
    @NonFinal
    @EqualsAndHashCode(callSuper = true)
    public static class UserCreatedTopic extends TopicCfgProps {

        @ConstructorBinding
        public UserCreatedTopic(
                @DefaultValue("user.created") @NotBlank String topicName,
                String subscriptionName,
                String consumerGroup,
                @DefaultValue("redis") @NotNull Transport transport) {
            super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
        }
    }

    @Value
    @NonFinal
    @EqualsAndHashCode(callSuper = true)
    public static class UserDeletedTopic extends TopicCfgProps {

        @ConstructorBinding
        public UserDeletedTopic(
                @DefaultValue("user.deleted") @NotBlank String topicName,
                String subscriptionName,
                String consumerGroup,
                @DefaultValue("redis") @NotNull Transport transport) {
            super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
        }
    }
}
