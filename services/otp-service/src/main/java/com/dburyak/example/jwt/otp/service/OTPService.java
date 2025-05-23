package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.email.PasswordResetEmailParam;
import com.dburyak.example.jwt.api.internal.email.SendEmailMsg;
import com.dburyak.example.jwt.api.internal.email.StandardTemplate;
import com.dburyak.example.jwt.api.internal.email.cfg.EmailMsgProperties;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.api.internal.otp.CreateOTPForRegisteredUserMsg;
import com.dburyak.example.jwt.api.internal.user.UserServiceClient;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.otp.domain.OTP;
import com.dburyak.example.jwt.otp.domain.OTPType;
import com.dburyak.example.jwt.otp.err.AnonymousOTPNotFoundException;
import com.dburyak.example.jwt.otp.err.OTPNotFoundException;
import com.dburyak.example.jwt.otp.repository.OTPRepository;
import com.dburyak.example.jwt.otp.service.converter.OTPConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Log4j2
public class OTPService {
    /// this should come from config, part of the same config used in OTPGenerator
    private static final Duration OTP_TTL = Duration.ofHours(1);
    private final OTPConverter otpConverter;
    private final OTPRepository otpRepository;
    private final OTPGenerator otpGenerator;
    private final UserServiceClient userServiceClient;
    private final PointToPointMsgQueue msgQueue;
    private final String emailTopic;

    private final Map<OTPType, String> emailTemplateForOtpType = Map.of(
            OTPType.PASSWORD_RESET, StandardTemplate.PASSWORD_RESET.getTemplateName()
    );

    public OTPService(OTPConverter otpConverter,
            OTPRepository otpRepository,
            OTPGenerator otpGenerator,
            UserServiceClient userServiceClient,
            PointToPointMsgQueue msgQueue,
            EmailMsgProperties emailMsgProps) {
        this.otpConverter = otpConverter;
        this.otpRepository = otpRepository;
        this.otpGenerator = otpGenerator;
        this.userServiceClient = userServiceClient;
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
        var email = otpConverter.toDomainEmail(req.getEmail());
        var code = otpGenerator.generate(req.getTenantUuid(), otpType, req.getLocale());
        var otp = OTP.builder()
                .tenantUuid(req.getTenantUuid())
                .uuid(UUID.randomUUID())
                .deviceId(req.getDeviceId())
                .externalId(email)
                .type(otpType)
                .code(code)
                .expiresAt(Instant.now().plus(OTP_TTL))
                .build();
        var oldOtp = otpRepository.insertOrReplaceByTenantUuidAndExternalIdAndDeviceIdAndType(otp);
        if (oldOtp != null && !isExpired(oldOtp)) {
            log.debug("replaced valid anonymous otp: oldOtp={}, newOtp={}", oldOtp, otp);
        }
        sendEmail(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.OTP findByTenantUuidAndUserUuidAndDeviceIdAndType(
            UUID tenantUuid, UUID userUuid, String deviceId, com.dburyak.example.jwt.api.internal.otp.OTPType type) {
        var typeDomain = otpConverter.toDomain(type);
        var otp = otpRepository.findByTenantUuidAndUserUuidAndDeviceIdAndTypeAndExpiresAtBefore(
                tenantUuid, userUuid, deviceId, typeDomain, Instant.now());
        if (otp == null || isExpired(otp)) {
            throw new OTPNotFoundException(userUuid, deviceId, type);
        }
        return otpConverter.toApiModel(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.OTP findByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCode(
            UUID tenantUuid, UUID userUuid, String deviceId, com.dburyak.example.jwt.api.internal.otp.OTPType type,
            String otpCode) {
        var typeDomain = otpConverter.toDomain(type);
        var otp = otpRepository.findByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(
                tenantUuid, userUuid, deviceId, typeDomain, otpCode, Instant.now());
        if (otp == null || isExpired(otp)) {
            throw new OTPNotFoundException(userUuid, deviceId, type, otpCode);
        }
        return otpConverter.toApiModel(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.OTP findByTenantUuidAndEmailAndDeviceIdAndType(
            UUID tenantUuid, String email, String deviceId, com.dburyak.example.jwt.api.internal.otp.OTPType type) {
        var typeDomain = otpConverter.toDomain(type);
        var emailDomain = otpConverter.toDomainEmail(email);
        var otp = otpRepository.findByTenantUuidAndExternalIdAndDeviceIdAndTypeAndExpiresAtBefore(tenantUuid,
                emailDomain, deviceId, typeDomain, Instant.now());
        if (otp == null || isExpired(otp)) {
            throw new AnonymousOTPNotFoundException(deviceId, type);
        }
        return otpConverter.toApiModel(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.OTP claimByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCode(
            UUID tenantUuid, UUID userUuid, String deviceId, com.dburyak.example.jwt.api.internal.otp.OTPType type,
            String otpCode) {
        var typeDomain = otpConverter.toDomain(type);
        var otp = otpRepository.findAndDeleteByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(
                tenantUuid, userUuid, deviceId, typeDomain, otpCode, Instant.now());
        if (otp == null || isExpired(otp)) {
            throw new OTPNotFoundException(userUuid, deviceId, type, otpCode);
        }
        return otpConverter.toApiModel(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.OTP claimByTenantUuidAndEmailAndDeviceIdAndTypeAndCode(
            UUID tenantUuid, String email, String deviceId, com.dburyak.example.jwt.api.internal.otp.OTPType type,
            String otpCode) {
        var typeDomain = otpConverter.toDomain(type);
        var emailDomain = otpConverter.toDomainEmail(email);
        var otp = otpRepository.findAndDeleteByTenantUuidAndExternalIdAndDeviceIdAndTypeAndCodeAndExpiresAtBefore(
                tenantUuid, emailDomain, deviceId, typeDomain, otpCode, Instant.now());
        if (otp == null || isExpired(otp)) {
            throw new AnonymousOTPNotFoundException(deviceId, type, otpCode);
        }
        return otpConverter.toApiModel(otp);
    }

    private boolean isExpired(OTP otp) {
        return otp.getExpiresAt().isBefore(Instant.now());
    }

    private void sendEmail(OTP otp) {
        var emailTemplate = emailTemplateForOtpType.get(otp.getType());
        if (isBlank(emailTemplate)) {
            throw new IllegalArgumentException("no email template for otp type: " + otp.getType());
        }
        var tenantUuid = otp.getTenantUuid();
        var userUuid = otp.getUserUuid();
        var email = userServiceClient.getContactInfo(tenantUuid, userUuid).getEmail();
        var emailMsg = SendEmailMsg.builder()
                .tenantUuid(otp.getTenantUuid())
                .template(emailTemplateForOtpType.get(otp.getType()))
                .to(email)
                .params(buildEmailParams(otp))
                .build();
        msgQueue.publish(emailTopic, emailMsg);
    }

    private Map<String, String> buildEmailParams(OTP otp) {
        // in a real app we would also have composed the link here to be sent in the email, and this link would
        // include OTP code as a query param
        return switch (otp.getType()) {
            case PASSWORD_RESET -> Map.of(
                    PasswordResetEmailParam.OTP.getParamName(), otp.getCode(),
                    PasswordResetEmailParam.LINK.getParamName(),
                    "https://example.com/password-reset/" + otp.getUserUuid() + "?otp=" +
                            otp.getCode() + "&" + QueryParams.TENANT_UUID + "=" + otp.getTenantUuid()
            );
            default -> throw new IllegalArgumentException("no email template for otp type: " + otp.getType());
        };
    }
}
