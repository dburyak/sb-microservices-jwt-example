package com.dburyak.example.jwt.user.service;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.internal.auth.AuthServiceClientInternal;
import com.dburyak.example.jwt.api.internal.otp.CreateEmailOTPForAnonymousUserMsg;
import com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.api.internal.otp.OTPServiceClientInternal;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPMsgProperties;
import com.dburyak.example.jwt.api.internal.user.UserCreatedMsg;
import com.dburyak.example.jwt.api.internal.user.cfg.UserMsgProperties;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.api.user.CreateRegistrationOtpViaEmail;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.api.user.UserWithRoles;
import com.dburyak.example.jwt.lib.auth.Role;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.user.err.UserAlreadyExistsException;
import com.dburyak.example.jwt.user.repository.UserRepository;
import com.dburyak.example.jwt.user.service.converter.UserConverter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final AuthServiceClientInternal authServiceClientInternal;
    private final PointToPointMsgQueue msgQueue;
    private final String anonymousEmailOtpTopic;
    private final OTPServiceClientInternal otpServiceClient;
    private final String userCreatedTopic;

    public UserService(
            UserRepository userRepository,
            UserConverter userConverter,
            AuthServiceClientInternal authServiceClientInternal,
            PointToPointMsgQueue msgQueue,
            OTPMsgProperties otpMsgProps,
            UserMsgProperties userMsgProps,
            OTPServiceClientInternal otpServiceClient) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.authServiceClientInternal = authServiceClientInternal;
        this.msgQueue = msgQueue;
        this.anonymousEmailOtpTopic = otpMsgProps.getTopics().getCreateOTPForAnonymousUser().getTopicName();
        this.userCreatedTopic = userMsgProps.getTopics().getUserCreated().getTopicName();
        this.otpServiceClient = otpServiceClient;
    }

    public void createRegistrationOtp(UUID tenantUuid, CreateRegistrationOtpViaEmail req) {
        if (userRepository.existsByExternalIdEmail(tenantUuid, req.getEmail())) {
            throw new UserAlreadyExistsException(req.getEmail());
        }
        var otpReqMsg = CreateEmailOTPForAnonymousUserMsg.builder()
                .type(ExternallyIdentifiedOTP.Type.REGISTRATION_WITH_EMAIL)
                .locale(req.getLocale())
                .deviceId(req.getDeviceId())
                .externalId(new ExternalId(req.getEmail()))
                .build();
        msgQueue.publish(anonymousEmailOtpTopic, otpReqMsg, tenantUuid);
    }

    public User createViaRegistration(UUID tenantUuid, String deviceId, User req, String registrationCode) {
        otpServiceClient.claimExternallyIdentifiedOTP(tenantUuid, deviceId,
                ExternallyIdentifiedOTP.Type.REGISTRATION_WITH_EMAIL, registrationCode, req.getExternalId());
        var savedUser = createUser(tenantUuid, Set.of(Role.USER.getName()), req);
        return userConverter.toApiModel(savedUser);
    }

    public User createByManager(UUID tenantUuid, UserWithRoles req) {
        var savedUser = createUser(tenantUuid, req.getRoles(), req);
        return userConverter.toApiModel(savedUser);
    }

    public User createBySystem(UUID tenantUuid, Set<String> roles, User req) {
        var savedUser = createUser(tenantUuid, roles, req);
        return userConverter.toApiModel(savedUser);
    }

    private com.dburyak.example.jwt.user.domain.User createUser(UUID tenantUuid, Set<String> roles, User req) {
        var user = userConverter.toDomain(req, tenantUuid);
        user.setUuid(UUID.randomUUID());
        var savedUser = userRepository.save(user);

        // We create user in auth-service separately via rest call instead of using message queue.
        // It is dangerous from a security perspective to publish password in plain text. Firstly, because every
        // subscriber will receive the password, even though they don't need it. Secondly, realistically message
        // queue will be durable and the password may be stored in plain text for a long time.
        authServiceClientInternal.createUser(tenantUuid, userConverter.toApiModelAuth(req, savedUser, roles));

        publishUserCreatedEvent(req, savedUser, roles);
        return savedUser;
    }

    public void deleteByUsernameByManager(UUID tenantUuid, String username) {
        var numDeleted = userRepository.deleteByTenantUuidAndUsername(tenantUuid, username);
        if (numDeleted == 0) {
            throw new NotFoundException("User(username=%s)".formatted(username));
        }
    }

    public void deleteByUuidByManager(UUID tenantUuid, UUID userUuid) {
        var numDeleted = userRepository.deleteByTenantUuidAndUuid(tenantUuid, userUuid);
        if (numDeleted == 0) {
            throw new NotFoundException("User(uuid=%s)".formatted(userUuid));
        }
    }

    public void deleteAllByTenantUuidWithoutNotification(UUID tenantUuid) {
        userRepository.deleteAllByTenantUuid(tenantUuid);
    }

    private void publishUserCreatedEvent(User reqUser, com.dburyak.example.jwt.user.domain.User domainUser,
            Set<String> roles) {
        var userCreatedMsg = UserCreatedMsg.builder()
                .tenantUuid(domainUser.getTenantUuid())
                .userUuid(domainUser.getUuid())
                .username(reqUser.getUsername())
                .roles(roles)
                .build();
        msgQueue.publish(userCreatedTopic, userCreatedMsg);
    }

    public User findByUuid(UUID tenantUuid, UUID userUuid) {
        var user = userRepository.findByTenantUuidAndUuid(tenantUuid, userUuid);
        if (user == null) {
            throw new NotFoundException("User(uuid=%s)".formatted(userUuid));
        }
        return userConverter.toApiModel(user);
    }

    public User findByUsername(UUID tenantUuid, String username) {
        var user = userRepository.findByTenantUuidAndUsername(tenantUuid, username);
        if (user == null) {
            throw new NotFoundException("User(username=%s)".formatted(username));
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
