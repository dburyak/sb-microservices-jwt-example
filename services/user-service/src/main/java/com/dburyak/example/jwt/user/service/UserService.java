package com.dburyak.example.jwt.user.service;

import com.dburyak.example.jwt.api.internal.auth.AuthServiceClientInternal;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.api.internal.otp.OTPServiceClientInternal;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPMsgProperties;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.api.user.CreateRegistrationOtpViaEmail;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.user.err.UserAlreadyExistsException;
import com.dburyak.example.jwt.user.repository.UserRepository;
import com.dburyak.example.jwt.user.service.converter.UserConverter;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.dburyak.example.jwt.api.internal.otp.OTPType.EMAIL_REGISTRATION;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final AuthServiceClientInternal authServiceClientInternal;
    private final PointToPointMsgQueue msgQueue;
    private final String anonymousEmailOtpTopic;
    private final OTPServiceClientInternal otpServiceClient;

    public UserService(UserRepository userRepository,
            UserConverter userConverter,
            AuthServiceClientInternal authServiceClientInternal,
            PointToPointMsgQueue msgQueue,
            OTPMsgProperties otpMsgProps,
            OTPServiceClientInternal otpServiceClient) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.authServiceClientInternal = authServiceClientInternal;
        this.msgQueue = msgQueue;
        this.anonymousEmailOtpTopic = otpMsgProps.getTopics().getCreateOTPForAnonymousUser().getTopicName();
        this.otpServiceClient = otpServiceClient;
    }

    public void createRegistrationOtp(UUID tenantUuid, CreateRegistrationOtpViaEmail req) {
        if (userRepository.existsByContactInfoEmail(tenantUuid, req.getEmail())) {
            throw new UserAlreadyExistsException(req.getEmail());
        }
        var otpReqMsg = CreateEmailOTPForAnonymousUserMsg.builder()
                .type(EMAIL_REGISTRATION)
                .locale(req.getLocale())
                .tenantUuid(tenantUuid)
                .deviceId(req.getDeviceId())
                .email(req.getEmail())
                .build();
        msgQueue.publish(anonymousEmailOtpTopic, otpReqMsg);
    }

    public User createViaRegistration(UUID tenantUuid, User req, String registrationCode) {
        otpServiceClient.claimOTPByTypeAndCode(tenantUuid, req.)
        var savedUser = createUser(tenantUuid, req);
        return userConverter.toApiModel(savedUser);
    }

    public User createByManager(UUID tenantUuid, User req) {
        var savedUser = createUser(tenantUuid, req);
        return userConverter.toApiModel(savedUser);
    }

    private com.dburyak.example.jwt.user.domain.User createUser(UUID tenantUuid, User req) {
        var user = userConverter.toDomain(req, tenantUuid);
        user.setUuid(UUID.randomUUID());
        var savedUser = userRepository.save(user);
        publishUserCreatedEvent(tenantUuid, req, savedUser);
        return savedUser;
    }

    public void deleteAllByTenantUuid(UUID tenantUuid) {
        userRepository.deleteAllByTenantUuid(tenantUuid);
    }

    private void publishUserCreatedEvent(UUID tenantUuid, User reqUser,
            com.dburyak.example.jwt.user.domain.User domainUser) {
        // in a real-world app we'd rather publish it as an event, and let the relevant services handle it
        authServiceClientInternal.createUser(tenantUuid, userConverter.toApiModelAuth(reqUser, domainUser));
    }

    public User findByUuid(UUID tenantUuid, UUID userUuid) {
        var user = userRepository.findByTenantUuidAndUuid(tenantUuid, userUuid);
        if (user == null) {
            throw new NotFoundException("User(uuid=%s)".formatted(userUuid));
        }
        return userConverter.toApiModel(user);
    }

    public ContactInfo findContactInfoByUuid(UUID tenantUuid, UUID userUuid) {
        var contactInfo = userRepository.findContactInfoByTenantUuidAndUuid(tenantUuid, userUuid);
        if (contactInfo == null) {
            throw new NotFoundException("User.ContactInfo(uuid=%s)".formatted(userUuid));
        }
        return userConverter.toApiModel(contactInfo);
    }
}
