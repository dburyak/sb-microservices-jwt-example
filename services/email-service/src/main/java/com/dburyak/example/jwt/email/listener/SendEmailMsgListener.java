package com.dburyak.example.jwt.email.listener;

import com.dburyak.example.jwt.api.internal.email.SendEmailMsg;
import com.dburyak.example.jwt.api.internal.email.cfg.EmailMsgProperties;
import com.dburyak.example.jwt.email.service.EmailService;
import com.dburyak.example.jwt.lib.msg.Msg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static com.dburyak.example.jwt.email.cfg.Authorities.SEND_EMAIL;

@Component
@RequiredArgsConstructor
public class SendEmailMsgListener {
    private final EmailMsgProperties props;
    private final PointToPointMsgQueue msgQueue;
    private final EmailService emailService;

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        subscribeToSendEmailMessages();
    }

    private void subscribeToSendEmailMessages() {
        var topic = props.getTopics().getSendEmail().getTopicName();
        var consumerGroup = props.getConsumerGroup();
        Predicate<Msg<SendEmailMsg>> accessCheck = msg ->
                msg.getAuth() != null
                        && msg.getAuth().isAuthenticated()
                        && msg.getAuth().getAuthorityNames().contains(SEND_EMAIL);
        msgQueue.subscribe(topic, consumerGroup, accessCheck,
                msg -> emailService.sendEmail(msg.getData()));
    }
}
