package com.dburyak.example.jwt.user.listener;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.internal.tenant.TenantCreatedMsg;
import com.dburyak.example.jwt.api.internal.tenant.TenantDeletedMsg;
import com.dburyak.example.jwt.api.internal.tenant.cfg.TenantMsgProperties;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.lib.msg.Msg;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;

import static com.dburyak.example.jwt.lib.auth.Role.ADMIN;
import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.SA;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;

@Component
public class TenantMsgListener {
    private final String serviceName;
    private final TenantMsgProperties tenantMsgProps;
    private final PointToPointMsgQueue msgQueue;
    private final UserService userService;

    public TenantMsgListener(
            @Value("${spring.application.name}") String serviceName,
            TenantMsgProperties tenantMsgProps,
            PointToPointMsgQueue msgQueue,
            UserService userService) {
        this.serviceName = serviceName;
        this.tenantMsgProps = tenantMsgProps;
        this.msgQueue = msgQueue;
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        subscribeToTenantCreatedEvents();
        subscribeToTenantDeletedEvents();
    }

    private void subscribeToTenantCreatedEvents() {
        var topic = firstNonBlank(
                tenantMsgProps.getTopics().getTenantCreated().getSubscriptionName(),
                tenantMsgProps.getTopics().getTenantCreated().getTopicName());
        var consumerGroup = firstNonBlank(
                tenantMsgProps.getTopics().getTenantCreated().getConsumerGroup(),
                tenantMsgProps.getConsumerGroup(),
                serviceName);
        Predicate<Msg<TenantCreatedMsg>> accessCheck = msg -> {
            var auth = msg.getAuth();
            return auth.isAuthenticated() && auth.getAuthorityNames().contains(SA);
        };
        msgQueue.subscribe(topic, consumerGroup, accessCheck, msg -> {
            // create tenant admin user
            var tenantUuid = msg.getData().getUuid();
            var adminEmail = msg.getData().getAdminEmail();
            userService.createBySystem(tenantUuid, Set.of(ADMIN.getName()), User.builder()
                    .externalId(new ExternalId(adminEmail))
                    .username("admin")
                    .displayName("admin")
                    .profileIcon("admin")
                    .contactInfo(new ContactInfo(adminEmail))
                    .build());
        });
    }

    private void subscribeToTenantDeletedEvents() {
        var topic = firstNonBlank(
                tenantMsgProps.getTopics().getTenantDeleted().getSubscriptionName(),
                tenantMsgProps.getTopics().getTenantDeleted().getTopicName());
        var consumerGroup = firstNonBlank(
                tenantMsgProps.getTopics().getTenantCreated().getConsumerGroup(),
                tenantMsgProps.getConsumerGroup(),
                serviceName);
        Predicate<Msg<TenantDeletedMsg>> accessCheck = msg -> {
            var auth = msg.getAuth();
            return auth.isAuthenticated() && auth.getAuthorityNames().contains(SA);
        };
        msgQueue.subscribe(topic, consumerGroup, accessCheck, msg -> {
            var deletedTenantUuid = msg.getData().getUuid();
            // deleting all the users of the tenant does not require a separate notification, other services will handle
            // the original tenant deletion event themselves
            userService.deleteAllByTenantUuidWithoutNotification(deletedTenantUuid);
        });
    }
}
