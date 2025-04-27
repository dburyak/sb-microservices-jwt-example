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
        CreateOTPTopic createOTP;

        @ConstructorBinding
        public Topics(
                @DefaultValue CreateOTPTopic createOTP) {
            this.createOTP = createOTP;
        }

        @Value
        @NonFinal
        @EqualsAndHashCode(callSuper = true)
        public static class CreateOTPTopic extends TopicCfgProps {

            @ConstructorBinding
            public CreateOTPTopic(
                    @DefaultValue("otp.create") String topicName,
                    String subscriptionName,
                    String consumerGroup,
                    @DefaultValue("redis") @NotNull Transport transport) {
                super(topicName, isNotBlank(subscriptionName) ? subscriptionName : topicName, consumerGroup, transport);
            }
        }
    }
}
