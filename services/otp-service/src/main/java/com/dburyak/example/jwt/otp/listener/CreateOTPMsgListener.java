package com.dburyak.example.jwt.otp.listener;

import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.api.internal.otp.CreateOTPForRegisteredUserMsg;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPMsgProperties;
import com.dburyak.example.jwt.lib.msg.Msg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.dburyak.example.jwt.otp.service.OTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Log4j2
public class CreateOTPMsgListener {
    private final CreateOTPMsgListener self;
    private final OTPMsgProperties props;
    private final PointToPointMsgQueue msgQueue;
    private final OTPService otpService;
    private final RequestUtil requestUtil;

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        subscribeToCreateOTPForRegisteredUserMessages();
    }

    public void createOTPForRegisteredUser(UUID tenantUuid, UUID userUuid, String deviceId,
            @Valid Msg<CreateOTPForRegisteredUserMsg> msg) {
        otpService.createForRegisteredUser(tenantUuid, userUuid, deviceId, msg.getData());
    }

    public void createOTPForAnonymousUser(@Valid Msg<CreateEmailOTPForAnonymousUserMsg> msg) {
        otpService.createForAnonymousUser(msg.getData());
    }

    private void subscribeToCreateOTPForRegisteredUserMessages() {
        var topic = props.getTopics().getCreateOTPForRegisteredUser().getTopicName();
        var consumerGroup = props.getConsumerGroup();
        Predicate<Msg<CreateOTPForRegisteredUserMsg>> accessCheck = msg -> {
            // TODO: add security check
            log.warn("auth check is not implemented, skipping: msg={}", msg);
            return true;
        };
        msgQueue.subscribe(topic, consumerGroup, accessCheck, msg -> {
            var tenandUuid = requestUtil.getTenantUuid();
            var userUuid = requestUtil.getUserUuid();
            var deviceId = requestUtil.getDeviceId();
            self.createOTPForRegisteredUser(tenandUuid, userUuid, deviceId, msg);
        });
    }

    private void subscribeToCreateOTPForAnonymousUserMessages() {
        var topic = props.getTopics().getCreateOTPForAnonymousUser().getTopicName();
        var consumerGroup = props.getConsumerGroup();
        Predicate<Msg<CreateEmailOTPForAnonymousUserMsg>> accessCheck = msg -> {
            // TODO: add security check
            log.warn("auth check is not implemented, skipping: msg={}", msg);
            return true;
        };
        msgQueue.subscribe(topic, consumerGroup, accessCheck, self::createOTPForAnonymousUser);
    }
}
