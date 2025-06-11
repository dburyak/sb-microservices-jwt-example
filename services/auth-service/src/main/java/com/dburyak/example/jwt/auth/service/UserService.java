package com.dburyak.example.jwt.auth.service;

import com.dburyak.example.jwt.api.auth.CreatePasswordResetOTPRequest;
import com.dburyak.example.jwt.api.auth.PasswordChangeRequest;
import com.dburyak.example.jwt.api.auth.PasswordResetRequest;
import com.dburyak.example.jwt.api.internal.auth.User;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForRegisteredUserMsg;
import com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.api.internal.otp.OTPServiceClientInternal;
import com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPMsgProperties;
import com.dburyak.example.jwt.api.internal.user.UserServiceClient;
import com.dburyak.example.jwt.auth.err.BadPasswordException;
import com.dburyak.example.jwt.auth.err.ExternallyIdentifiedUserNotFoundException;
import com.dburyak.example.jwt.auth.err.UserNotFoundException;
import com.dburyak.example.jwt.auth.repository.UserRepository;
import com.dburyak.example.jwt.auth.service.converter.UserConverter;
import com.dburyak.example.jwt.lib.auth.Role;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.lib.req.Headers.TENANT_UUID;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
public class UserService {
    private final UserConverter converter;
    private final UserRepository repository;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final OTPServiceClientInternal otpServiceClient;
    private final PointToPointMsgQueue msgQueue;
    private final String anonymousUserCreateOtpTopic;
    private final String registeredUserCreateOtpTopic;

    public UserService(UserConverter converter,
            UserRepository repository,
            PasswordValidator passwordValidator,
            PasswordEncoder passwordEncoder,
            UserServiceClient userServiceClient,
            OTPServiceClientInternal otpServiceClient,
            PointToPointMsgQueue msgQueue,
            OTPMsgProperties otpMsgProperties) {
        this.converter = converter;
        this.repository = repository;
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
        this.otpServiceClient = otpServiceClient;
        this.msgQueue = msgQueue;
        this.anonymousUserCreateOtpTopic = otpMsgProperties.getTopics().getCreateOTPForAnonymousUser().getTopicName();
        this.registeredUserCreateOtpTopic = otpMsgProperties.getTopics().getCreateOTPForRegisteredUser().getTopicName();
    }

    public User create(UUID tenantUUid, User req, Set<Role> roles) {
        var user = converter.toDomain(req, tenantUUid);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(roles.stream().map(Role::getName).collect(toSet()));
        var savedUser = repository.save(user);
        return converter.toApiModel(savedUser);
    }

    public void createPasswordResetOTP(UUID tenantUuid, CreatePasswordResetOTPRequest req) {
        var externalIdDomain = converter.toDomain(req.getExternalId());
        var user = repository.findByTenantUuidAndExternalIdEmail(tenantUuid, externalIdDomain.getEmail());
        if (user == null) {
            throw new ExternallyIdentifiedUserNotFoundException(req.getExternalId());
        }
        var createOtpMsg = CreateEmailOTPForAnonymousUserMsg.builder()
                .locale(req.getLocale())
                .externalId(req.getExternalId())
                .deviceId(req.getDeviceId())
                .type(ExternallyIdentifiedOTP.Type.PASSWORD_RESET)
                .build();
        msgQueue.publish(anonymousUserCreateOtpTopic, createOtpMsg, tenantUuid);
    }

    public void resetPassword(UUID tenantUuid, PasswordResetRequest req) {
        var newPasswordDomain = converter.toDomainPassword(req.getNewPassword());
        if (!passwordValidator.isValid(newPasswordDomain)) {
            // in a real app, we would elaborate on the specifics of the password validation failure
            throw new BadPasswordException("password does not meet security requirements");
        }
        var otpMatched = otpServiceClient.claimExternallyIdentifiedOTP(tenantUuid, req.getDeviceId(),
                ExternallyIdentifiedOTP.Type.PASSWORD_RESET, req.getOtp(), req.getExternalId());
        if (!otpMatched) {
            throw new NotFoundException("OTP(deviceId=%s)".formatted(req.getDeviceId()));
        }
        var externalIdDomain = converter.toDomain(req.getExternalId());
        var newPasswordEncoded = passwordEncoder.encode(newPasswordDomain);
        var updated = repository.updatePasswordByExternalIdEmail(tenantUuid, externalIdDomain.getEmail(),
                newPasswordEncoded);
        if (!updated) {
            // In a real app we would care about such data inconsistency and would handle it more seriously.
            // PII should never be logged in plain form, email here should be masked or hashed for logging.
            log.warn("failed to find user for password reset with valid OTP: tenantUuid={}, externalId={}",
                    tenantUuid, externalIdDomain.getEmail());
        }
    }

    public void createPasswordChangeOTP(UUID tenantUuid, UUID userUuid) {
        var user = repository.findByTenantUuidAndUuid(tenantUuid, userUuid);
        if (user == null) {
            throw new UserNotFoundException(userUuid);
        }
        var contactDetails = userServiceClient.getContactInfo(tenantUuid, userUuid);
        // tenantUuid, userUuid and deviceId are in the auth token and will be passed along with the
        // message in headers, so that's why the request DTO does not contain them
        var createOtpMsg = CreateEmailOTPForRegisteredUserMsg.builder()
                .locale("en") // TODO: fetch locale from user profile/preferences (probably in user-service)
                .type(RegisteredUserOTP.Type.PASSWORD_RESET)
                .email(contactDetails.getEmail())
                .build();
        msgQueue.publish(registeredUserCreateOtpTopic, createOtpMsg);
    }

    public void changePassword(UUID tenantUuid, UUID userUuid, String deviceId, PasswordChangeRequest req) {
        var newPasswordDomain = converter.toDomainPassword(req.getNewPassword());
        if (!passwordValidator.isValid(newPasswordDomain)) {
            // in a real app, we would elaborate on the specifics of the password validation failure
            throw new BadPasswordException("password does not meet security requirements");
        }
        // (1)
        var user = repository.findByTenantUuidAndUuid(tenantUuid, userUuid);
        if (user == null) {
            throw new UserNotFoundException(userUuid);
        }
        var oldPasswordDomain = converter.toDomainPassword(req.getOldPassword());
        if (!passwordEncoder.matches(oldPasswordDomain, user.getPassword())) {
            throw new BadPasswordException("old password does not match");
        }
        var otpMatched = otpServiceClient.claimRegisteredUserOTP(RegisteredUserOTP.Type.PASSWORD_RESET, req.getOtp());
        if (!otpMatched) {
            throw new NotFoundException("OTP(userUuid=%s, deviceId=%s)".formatted(userUuid, deviceId));
        }
        var newPasswordEncoded = passwordEncoder.encode(newPasswordDomain);
        // (2)
        // Mongo is not transactional, so in general case, there may be interleaving operations between (1) and (2).
        // And in general case, we would tackle it with optimistic locking or other more precise query that includes
        // previuos state (old password for example) in the query condition.
        // BUT in this specific case, interleaving operations will not have any negative effect, so we don't care.
        // Specifically, this is because:
        //  - update query updates password only and new password is not calculated based on the old password, no any
        //  possible data loss or inconsistency is possible due to interleaving operations
        //  - from business logic perspective, if there are two interleaving requests to change password, the sane
        //   expected behavior would be that the last request wins, which is exactly what will happen here - last update
        //   query will always overwrite password, and if we got here then the request passed both checks - old password
        //   matched and OTP was valid
        var updated = repository.updatePasswordByUuid(tenantUuid, userUuid, newPasswordEncoded);
        if (!updated) {
            // in a real app we would do optimistic lock retry in this case
            log.warn("failed to update password for user: tenantUuid={}, userUuid={}", tenantUuid, userUuid);
        }
    }

    public void deleteAllByTenantUuid(UUID tenantUuid) {
        repository.deleteAllByTenantUuid(tenantUuid);
    }
}
