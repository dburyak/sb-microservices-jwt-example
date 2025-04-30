package com.dburyak.example.jwt.api.internal.otp.cfg;

import com.dburyak.example.jwt.api.internal.common.msg.MsgProperties;
import com.dburyak.example.jwt.api.internal.common.msg.TopicCfgProps;
import com.dburyak.example.jwt.api.internal.common.msg.Transport;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ConfigurationProperties(prefix = "msg.otp")
@Validated
@Value
@EqualsAndHashCode(callSuper = true)
@NonFinal
public class OTPMsgProperties extends MsgProperties {
    Topics topics;

    @ConstructorBinding
    public OTPMsgProperties(
            String consumerGroup,
            @DefaultValue Topics topics) {
        super(consumerGroup);
        this.topics = topics;
    }

    @Value
    @NonFinal
    public static class Topics {
        CreateOTPForAnonymousUserTopic createOTPForAnonymousUser;
        CreateOTPForRegisteredUserTopic createOTPForRegisteredUser;

        @ConstructorBinding
        public Topics(
                @DefaultValue CreateOTPForAnonymousUserTopic createOTPForAnonymousUser,
                @DefaultValue CreateOTPForRegisteredUserTopic createOTPForRegisteredUser) {
            this.createOTPForAnonymousUser = createOTPForAnonymousUser;
            this.createOTPForRegisteredUser = createOTPForRegisteredUser;
        }

        @Value
        @NonFinal
        @EqualsAndHashCode(callSuper = true)
        public static class CreateOTPForAnonymousUserTopic extends TopicCfgProps {

            @ConstructorBinding
            public CreateOTPForAnonymousUserTopic(
                    @DefaultValue("otp.create-for-anonymous-user") String topicName,
                    String subscriptionName,
                    String consumerGroup,
                    @DefaultValue("redis") @NotNull Transport transport) {
                super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
            }
        }

        @Value
        @NonFinal
        @EqualsAndHashCode(callSuper = true)
        public static class CreateOTPForRegisteredUserTopic extends TopicCfgProps {

            @ConstructorBinding
            public CreateOTPForRegisteredUserTopic(
                    @DefaultValue("otp.create-for-registered-user") String topicName,
                    String subscriptionName,
                    String consumerGroup,
                    @DefaultValue("redis") @NotNull Transport transport) {
                super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
            }
        }
    }
}
