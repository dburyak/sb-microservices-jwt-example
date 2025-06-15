package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.email.PasswordResetEmailParam;
import com.dburyak.example.jwt.api.internal.email.RegistrationWithEmailParam;
import com.dburyak.example.jwt.api.internal.email.SendEmailMsg;
import com.dburyak.example.jwt.api.internal.email.StandardTemplate;
import com.dburyak.example.jwt.api.internal.email.cfg.EmailMsgProperties;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.otp.cfg.AllOTPProperties;
import com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.otp.err.ExternallyIdentifiedOTPNotFoundException;
import com.dburyak.example.jwt.otp.repository.ExternallyIdentifiedOTPRepository;
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
public class ExternallyIdentifiedOTPService {
    private final Duration defaultOtpTtl;
    private final Duration passwordResetOtpTtl;
    private final Duration registrationWithEmailOtpTtl;
    private final OTPConverter converter;
    private final ExternallyIdentifiedOTPRepository otpRepository;
    private final ExternallyIdentifiedOTPGenerator otpGenerator;
    private final PointToPointMsgQueue msgQueue;
    private final String emailTopic;

    private final Map<ExternallyIdentifiedOTP.Type, String> emailTemplateForOtpType = Map.of(
            ExternallyIdentifiedOTP.Type.PASSWORD_RESET, StandardTemplate.PASSWORD_RESET.getTemplateName(),
            ExternallyIdentifiedOTP.Type.REGISTRATION_WITH_EMAIL, StandardTemplate.PASSWORD_RESET.getTemplateName()
    );

    public ExternallyIdentifiedOTPService(
            AllOTPProperties props,
            OTPConverter otpConverter,
            ExternallyIdentifiedOTPRepository otpRepository,
            ExternallyIdentifiedOTPGenerator otpGenerator,
            PointToPointMsgQueue msgQueue,
            EmailMsgProperties emailMsgProps) {
        defaultOtpTtl = props.getDefaultCfg().getTtl();
        passwordResetOtpTtl = requireNonNullElse(props.getPasswordReset().getTtl(), defaultOtpTtl);
        registrationWithEmailOtpTtl = requireNonNullElse(props.getRegistrationWithEmail().getTtl(), defaultOtpTtl);
        this.converter = otpConverter;
        this.otpRepository = otpRepository;
        this.otpGenerator = otpGenerator;
        this.msgQueue = msgQueue;
        this.emailTopic = emailMsgProps.getTopics().getSendEmail().getTopicName();
    }

    public void createOTP(UUID tenantUuid, CreateEmailOTPForAnonymousUserMsg req) {
        var now = Instant.now();
        var externalId = converter.toDomain(req.getExternalId());
        var otpType = converter.toDomain(req.getType());
        var ttl = determineOtpTtl(otpType);
        var code = otpGenerator.generate(tenantUuid, otpType, req.getLocale());
        var otp = ExternallyIdentifiedOTP.builder()
                .tenantUuid(tenantUuid)
                .uuid(UUID.randomUUID())
                .externalId(externalId)
                .deviceId(req.getDeviceId())
                .type(otpType)
                .code(code)
                .expiresAt(now.plus(ttl))
                .build();
        var oldOtp = otpRepository.insertOrReplaceByTenantUuidAndEmailAndDeviceIdAndType(otp);
        if (oldOtp != null && !isExpired(oldOtp, now)) {
            log.debug("replaced valid otp: oldOtp={}, newOtp={}", oldOtp, otp);
        }
        sendEmail(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP claimOTP(UUID tenantUuid,
            ExternalId externalId, String deviceId,
            com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP.Type type,
            String code) {
        var now = Instant.now();
        var typeDomain = converter.toDomain(type);
        var externalIdDomain = converter.toDomain(externalId);
        var otp = otpRepository.findAndDeleteByTenantUuidAndEmailAndDeviceIdAndTypeAndCodeAndExpiresAtAfter(
                tenantUuid, externalIdDomain.getEmail(), deviceId, typeDomain, code, now);
        if (otp == null || isExpired(otp, now)) {
            throw new ExternallyIdentifiedOTPNotFoundException(deviceId, type);
        }
        return converter.toApiModel(otp);
    }

    public com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP findOTP(UUID tenantUuid,
            ExternalId externalId, String deviceId,
            com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP.Type type) {
        var now = Instant.now();
        var externalIdDomain = converter.toDomain(externalId);
        var typeDomain = converter.toDomain(type);
        var otp = otpRepository.findByTenantUuidAndEmailAndDeviceIdAndTypeAndExpiresAtAfter(tenantUuid,
                externalIdDomain.getEmail(), deviceId, typeDomain, now);
        if (otp == null || isExpired(otp, now)) {
            throw new ExternallyIdentifiedOTPNotFoundException(deviceId, type);
        }
        return converter.toApiModel(otp);
    }

    private Duration determineOtpTtl(ExternallyIdentifiedOTP.Type type) {
        return switch (type) {
            case PASSWORD_RESET -> passwordResetOtpTtl;
            case REGISTRATION_WITH_EMAIL -> registrationWithEmailOtpTtl;
            default -> defaultOtpTtl;
        };
    }

    private boolean isExpired(ExternallyIdentifiedOTP otp, Instant now) {
        return otp.getExpiresAt().isBefore(now);
    }

    private void sendEmail(ExternallyIdentifiedOTP otp) {
        var emailTemplate = emailTemplateForOtpType.get(otp.getType());
        if (isBlank(emailTemplate)) {
            throw new IllegalArgumentException("no email template for otp type: " + otp.getType());
        }
        var emailMsg = SendEmailMsg.builder()
                .template(emailTemplate)
                .to(otp.getExternalId().getEmail())
                .params(buildEmailParams(otp))
                .build();
        msgQueue.publish(emailTopic, emailMsg, otp.getTenantUuid());
    }

    private Map<String, String> buildEmailParams(ExternallyIdentifiedOTP otp) {
        // in a real app we would also have composed the link here to be sent in the email, and this link would
        // include OTP code as a query param
        return switch (otp.getType()) {
            case PASSWORD_RESET -> Map.of(
                    PasswordResetEmailParam.OTP.getParamName(), otp.getCode(),

                    // email should not be exposed in the url, so either the user has to re-type it again, or the
                    // frontend app should store it securely and fill it in the field on the page
                    PasswordResetEmailParam.LINK.getParamName(), "https://example.com/password/reset" +
                            "?otp=" + otp.getCode() + "&" +
                            QueryParams.TENANT_UUID + "=" + otp.getTenantUuid()
            );
            case REGISTRATION_WITH_EMAIL -> Map.of(
                    RegistrationWithEmailParam.OTP.getParamName(), otp.getCode(),

                    // email should not be exposed in the url, so either the user has to re-type it again, or the
                    // frontend app should store it securely and fill it in the field on the page
                    RegistrationWithEmailParam.LINK.getParamName(),
                    "https://example.com/registration/email" + "?otp=" +
                            otp.getCode() + "&" + QueryParams.TENANT_UUID + "=" + otp.getTenantUuid()
            );
            default -> throw new IllegalArgumentException("no email template for otp type: " + otp.getType());
        };
    }
}
