package com.dburyak.example.jwt.email.listener;

import com.dburyak.example.jwt.api.internal.email.SendEmailMsg;
import com.dburyak.example.jwt.api.internal.email.cfg.EmailMsgProperties;
import com.dburyak.example.jwt.email.service.EmailService;
import com.dburyak.example.jwt.lib.msg.Msg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static com.dburyak.example.jwt.email.cfg.Authorities.SEND_EMAIL;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;

@Component
public class SendEmailMsgListener {
    private final String serviceName;
    private final EmailMsgProperties props;
    private final PointToPointMsgQueue msgQueue;
    private final EmailService emailService;

    public SendEmailMsgListener(
            @Value("${spring.application.name}") String serviceName,
            EmailMsgProperties props,
            PointToPointMsgQueue msgQueue,
            EmailService emailService) {
        this.serviceName = serviceName;
        this.props = props;
        this.msgQueue = msgQueue;
        this.emailService = emailService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        subscribeToSendEmailMessages();
    }

    private void subscribeToSendEmailMessages() {
        var topic = StringUtils.firstNonBlank(
                props.getTopics().getSendEmail().getSubscriptionName(),
                props.getTopics().getSendEmail().getTopicName()
        );
        var consumerGroup = firstNonBlank(
                props.getTopics().getSendEmail().getConsumerGroup(),
                props.getConsumerGroup(),
                serviceName);
        Predicate<Msg<SendEmailMsg>> accessCheck = msg ->
                msg.getAuth() != null
                        && msg.getAuth().isAuthenticated()
                        && msg.getAuth().getAuthorityNames().contains(SEND_EMAIL);
        msgQueue.subscribe(topic, consumerGroup, accessCheck,
                msg -> emailService.sendEmail(msg.getData()));
    }
}
