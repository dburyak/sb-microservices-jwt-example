package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.api.internal.email.SendEmailMsg;
import com.dburyak.example.jwt.api.internal.email.StandardTemplate;
import com.dburyak.example.jwt.api.internal.email.cfg.EmailMsgProperties;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.api.internal.otp.CreateOTPForRegisteredUserMsg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.otp.domain.OTP;
import com.dburyak.example.jwt.otp.domain.OTPType;
import com.dburyak.example.jwt.otp.repository.OTPRepository;
import com.dburyak.example.jwt.otp.service.converter.OTPConverter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class OTPService {
    /// this should come from config, part of the same config used in OTPGenerator
    private static final Duration OTP_TTL = Duration.ofHours(1);
    private final OTPConverter otpConverter;
    private final OTPRepository otpRepository;
    private final OTPGenerator otpGenerator;
    private final PointToPointMsgQueue msgQueue;
    private final String emailTopic;
    private final UserSerCli

    private final Map<OTPType, String> emailTemplateForOtpType = Map.of(
            OTPType.PASSWORD_RESET, StandardTemplate.PASSWORD_RESET.getTemplateName()
    );

    public OTPService(OTPConverter otpConverter,
            OTPRepository otpRepository,
            OTPGenerator otpGenerator,
            PointToPointMsgQueue msgQueue,
            EmailMsgProperties emailMsgProps) {
        this.otpConverter = otpConverter;
        this.otpRepository = otpRepository;
        this.otpGenerator = otpGenerator;
        this.msgQueue = msgQueue;
        this.emailTopic = emailMsgProps.getTopics().getSendEmail().getTopicName();
    }

    public void createForRegisteredUser(UUID tenantUuid, UUID userUuid, String deviceId,
            CreateOTPForRegisteredUserMsg req) {
        var otpType = otpConverter.toDomain(req.getType());
        var code = otpGenerator.generate(tenantUuid, otpType, req.getLocale());
        var otp = OTP.builder()
                .tenantUuid(tenantUuid)
                .uuid(UUID.randomUUID())
                .userUuid(userUuid)
                .deviceId(deviceId)
                .type(otpType)
                .code(code)
                .expiresAt(Instant.now().plus(OTP_TTL))
                .build();
        var oldOtp = otpRepository.insertOrReplaceByTenantUuidAndUserUuidAndDeviceIdAndType(otp);
        if (oldOtp != null && !isExpired(oldOtp)) {
            log.debug("replaced valid otp: oldOtp={}, newOtp={}", oldOtp, otp);
        }
        sendEmail(otp);
    }

    public void createForAnonymousUser(CreateEmailOTPForAnonymousUserMsg req) {
        var otpType = otpConverter.toDomain(req.getType());
        var code = otpGenerator.generate(req.getTenantUuid(), otpType, req.getLocale());
        var otp = OTP.builder()
                .tenantUuid(req.getTenantUuid())
                .uuid(UUID.randomUUID())
                .deviceId(req.getDeviceId())
                .type(otpType)
                .code(code)
                .expiresAt(Instant.now().plus(OTP_TTL))
                .build();
        sendEmail(otp);
    }

    private boolean isExpired(OTP otp) {
        return otp.getExpiresAt().isBefore(Instant.now());
    }

    private void sendEmail(OTP otp) {
        var emailTemplate = emailTemplateForOtpType.get(otp.getType());
        if (StringUtils.isBlank(emailTemplate)) {
            throw new IllegalArgumentException("no email template for otp type: " + otp.getType());
        }
        var emailMsg = SendEmailMsg.builder()
                .tenantUuid(otp.getTenantUuid())
                .template(emailTemplateForOtpType.get(otp.getType()))
                .to(req.getEmail())
                .subject("Your OTP code")
                .body("Your OTP code is: " + otp.getCode())
                .build();
        msgQueue.publish(emailTopic, );
    }
}
