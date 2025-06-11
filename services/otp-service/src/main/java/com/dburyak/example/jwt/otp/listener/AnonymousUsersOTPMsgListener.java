package com.dburyak.example.jwt.otp.listener;

import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPMsgProperties;
import com.dburyak.example.jwt.lib.msg.Msg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.dburyak.example.jwt.otp.service.ExternallyIdentifiedOTPService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.firstNonBlank;

@Component
@Log4j2
public class AnonymousUsersOTPMsgListener {
    private AnonymousUsersOTPMsgListener self;
    private final String serviceName;
    private final OTPMsgProperties props;
    private final PointToPointMsgQueue msgQueue;
    private final ExternallyIdentifiedOTPService otpService;
    private final RequestUtil requestUtil;

    public AnonymousUsersOTPMsgListener(
            @Value("${spring.application.name}") String serviceName,
            OTPMsgProperties props,
            PointToPointMsgQueue msgQueue,
            ExternallyIdentifiedOTPService otpService,
            RequestUtil requestUtil) {
        this.serviceName = serviceName;
        this.props = props;
        this.msgQueue = msgQueue;
        this.otpService = otpService;
        this.requestUtil = requestUtil;
    }

    @PostConstruct
    public void injectSelf() {
        self = this; // trick to use annotation-based validations right in this class
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        subscribeToCreateOTPMessages();
    }

    public void createOTPForAnonymousUser(UUID tenantUuid, @Valid CreateEmailOTPForAnonymousUserMsg req) {
        otpService.createOTP(tenantUuid, req);
    }

    private void subscribeToCreateOTPMessages() {
        var topic = firstNonBlank(
                props.getTopics().getCreateOTPForAnonymousUser().getSubscriptionName(),
                props.getTopics().getCreateOTPForAnonymousUser().getTopicName());
        var consumerGroup = firstNonBlank(
                props.getTopics().getCreateOTPForAnonymousUser().getConsumerGroup(),
                props.getConsumerGroup(),
                serviceName);
        Predicate<Msg<CreateEmailOTPForAnonymousUserMsg>> accessCheck = msg -> {
            // TODO: add security check
            log.warn("auth check is not implemented, processing without check: msg={}", msg);
            return true;
        };
        msgQueue.subscribe(topic, consumerGroup, accessCheck, msg -> {
            var tenantUuid = requestUtil.getTenantUuid();
            self.createOTPForAnonymousUser(msg.getData());
        });
    }
}
