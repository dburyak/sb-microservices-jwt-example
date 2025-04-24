package com.dburyak.example.jwt.api.internal.email.cfg;

import com.dburyak.example.jwt.api.internal.common.msg.MsgProperties;
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

@ConfigurationProperties(prefix = "msg.email")
@Validated
@Value
@EqualsAndHashCode(callSuper = true)
@NonFinal
public class EmailMsgProperties extends MsgProperties {
    Topics topics;

    @ConstructorBinding
    public EmailMsgProperties(
            String consumerGroup,
            @DefaultValue Topics topics) {
        super(consumerGroup);
        this.topics = topics;
    }

    @Value
    @NonFinal
    public static class Topics {
        SendEmailTopic sendEmail;

        @ConstructorBinding
        public Topics(
                @DefaultValue SendEmailTopic sendEmail) {
            this.sendEmail = sendEmail;
        }

        @Value
        @NonFinal
        @EqualsAndHashCode(callSuper = true)
        public static class SendEmailTopic extends TopicCfgProps {

            @ConstructorBinding
            public SendEmailTopic(
                    @DefaultValue("email.send") @NotBlank String topicName,
                    String subscriptionName,
                    String consumerGroup,
                    @DefaultValue("redis") @NotNull Transport transport) {
                super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
            }
        }
    }
}
