package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.api.internal.email.StandardTemplate;
import com.dburyak.example.jwt.api.internal.email.cfg.EmailMsgProperties;
import com.dburyak.example.jwt.api.internal.otp.CreateOTPForRegisteredUserMsg;
import com.dburyak.example.jwt.api.internal.user.UserServiceClient;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.otp.cfg.AllOTPProperties;
import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP;
import com.dburyak.example.jwt.otp.repository.RegisteredUserOTPRepository;
import com.dburyak.example.jwt.otp.service.converter.OTPConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNullElse;

@Service
@Log4j2
public class RegisteredUserOTPService {
    private final Duration defaultOtpTtl;
    private final Duration passwordResetOtpTtl;
    private final OTPConverter converter;
    private final RegisteredUserOTPRepository otpRepository;
    private final RegisteredUserOTPGenerator otpGenerator;
    private final UserServiceClient userServiceClient;
    private final PointToPointMsgQueue msgQueue;
    private final String emailTopic;

    private final Map<RegisteredUserOTP.Type, String> emailTemplateForOtpType = Map.of(
            RegisteredUserOTP.Type.PASSWORD_RESET, StandardTemplate.PASSWORD_RESET.getTemplateName()
    );

    public RegisteredUserOTPService(
            AllOTPProperties props,
            OTPConverter otpConverter,
            RegisteredUserOTPRepository otpRepository,
            RegisteredUserOTPGenerator otpGenerator,
            UserServiceClient userServiceClient,
            PointToPointMsgQueue msgQueue,
            EmailMsgProperties emailMsgProps) {
        defaultOtpTtl = props.getDefaultCfg().getTtl();
        passwordResetOtpTtl = requireNonNullElse(props.getPasswordReset().getTtl(), defaultOtpTtl);
        this.converter = otpConverter;
        this.otpRepository = otpRepository;
        this.otpGenerator = otpGenerator;
        this.userServiceClient = userServiceClient;
        this.msgQueue = msgQueue;
        this.emailTopic = emailMsgProps.getTopics().getSendEmail().getTopicName();
    }

    public void createOTP(UUID tenantUuid, UUID userUuid, String deviceId, CreateOTPForRegisteredUserMsg req) {

    }
}
