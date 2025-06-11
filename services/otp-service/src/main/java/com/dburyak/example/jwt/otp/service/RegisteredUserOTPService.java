package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.email.PasswordChangeEmailParam;
import com.dburyak.example.jwt.api.internal.email.SendEmailMsg;
import com.dburyak.example.jwt.api.internal.email.StandardTemplate;
import com.dburyak.example.jwt.api.internal.email.cfg.EmailMsgProperties;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForRegisteredUserMsg;
import com.dburyak.example.jwt.api.internal.user.UserServiceClient;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.otp.cfg.AllOTPProperties;
import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP;
import com.dburyak.example.jwt.otp.err.RegisteredUserOTPNotFoundException;
import com.dburyak.example.jwt.otp.repository.RegisteredUserOTPRepository;
import com.dburyak.example.jwt.otp.service.converter.OTPConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNullElse;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
            RegisteredUserOTP.Type.CHANGE_PASSWORD, StandardTemplate.PASSWORD_RESET.getTemplateName()
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

    public void createOTP(UUID tenantUuid, UUID userUuid, String deviceId, CreateEmailOTPForRegisteredUserMsg req) {
        var now = Instant.now();
        var otpType = converter.toDomain(req.getType());
        var ttl = determineOtpTtl(otpType);
        var code = otpGenerator.generate(tenantUuid, otpType, req.getLocale());
        var otp = RegisteredUserOTP.builder()
                .tenantUuid(tenantUuid)
                .uuid(UUID.randomUUID())
                .userUuid(userUuid)
                .deviceId(deviceId)
                .type(otpType)
                .code(code)
                .expiresAt(now.plus(ttl))
                .build();
        var oldOtp = otpRepository.insertOrReplaceByTenantUuidAndUserUuidAndDeviceIdAndType(otp);
        if (oldOtp != null && !isExpired(oldOtp, now)) {
            log.debug("replaced valid otp: oldOtp={}, newOtp={}", oldOtp, otp);
        }
        sendEmail(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP claimOTP(UUID tenantUuid, UUID userUuid,
            String deviceId, com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP.Type type, String otpCode) {
        var now = Instant.now();
        var typeDomain = converter.toDomain(type);
        var otp = otpRepository.findAndDeleteByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(
                tenantUuid, userUuid, deviceId, typeDomain, otpCode, now);
        if (otp == null || isExpired(otp, now)) {
            throw new RegisteredUserOTPNotFoundException(userUuid, deviceId, type);
        }
        return converter.toApiModel(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP findOTP(UUID tenantUuid, UUID userUuid,
            String deviceId, com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP.Type type) {
        var now = Instant.now();
        var typeDomain = converter.toDomain(type);
        var otp = otpRepository.findByTenantUuidAndUserUuidAndDeviceIdAndTypeAndExpiresAtBefore(
                tenantUuid, userUuid, deviceId, typeDomain, now);
        if (otp == null || isExpired(otp, now)) {
            throw new RegisteredUserOTPNotFoundException(userUuid, deviceId, type);
        }
        return converter.toApiModel(otp);
    }

    private Duration determineOtpTtl(RegisteredUserOTP.Type type) {
        return switch (type) {
            case CHANGE_PASSWORD -> passwordResetOtpTtl;
            default -> defaultOtpTtl;
        };
    }

    private boolean isExpired(RegisteredUserOTP otp, Instant now) {
        return otp.getExpiresAt().isBefore(now);
    }

    private void sendEmail(RegisteredUserOTP otp) {
        var emailTemplate = emailTemplateForOtpType.get(otp.getType());
        if (isBlank(emailTemplate)) {
            throw new IllegalArgumentException("no email template for otp type: " + otp.getType());
        }
        var tenantUuid = otp.getTenantUuid();
        var userUuid = otp.getUserUuid();
        var email = userServiceClient.getContactInfo(tenantUuid, userUuid).getEmail();
        var emailMsg = SendEmailMsg.builder()
                .template(emailTemplate)
                .to(email)
                .params(buildEmailParams(otp))
                .build();
        msgQueue.publish(emailTopic, emailMsg);
    }

    private Map<String, String> buildEmailParams(RegisteredUserOTP otp) {
        // in a real app we would also have composed the link here to be sent in the email, and this link would
        // include OTP code as a query param
        return switch (otp.getType()) {
            case CHANGE_PASSWORD -> Map.of(
                    PasswordChangeEmailParam.OTP.getParamName(), otp.getCode(),
                    PasswordChangeEmailParam.LINK.getParamName(),
                    "https://example.com/password/change/" + otp.getUserUuid() + "?otp=" +
                            otp.getCode() + "&" + QueryParams.TENANT_UUID + "=" + otp.getTenantUuid()
            );
            default -> throw new IllegalArgumentException("no email template for otp type: " + otp.getType());
        };
    }
}
