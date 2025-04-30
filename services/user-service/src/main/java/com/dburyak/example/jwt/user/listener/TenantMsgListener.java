package com.dburyak.example.jwt.user.listener;

import com.dburyak.example.jwt.api.internal.tenant.TenantCreatedMsg;
import com.dburyak.example.jwt.api.internal.tenant.TenantDeletedMsg;
import com.dburyak.example.jwt.api.internal.user.UserCreatedMsg;
import com.dburyak.example.jwt.api.internal.tenant.cfg.TenantMsgProperties;
import com.dburyak.example.jwt.api.internal.user.cfg.UserMsgProperties;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.lib.auth.AppAuthentication;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import com.dburyak.example.jwt.user.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;

import static com.dburyak.example.jwt.lib.auth.Role.ADMIN;
import static com.dburyak.example.jwt.user.cfg.Authorities.SA;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;

@Component
public class TenantMsgListener {
    private final TenantMsgProperties tenantMsgProps;
    private final UserMsgProperties userMsgProps;
    private final PointToPointMsgQueue msgQueue;
    private final UserService userService;

    public TenantMsgListener(
            TenantMsgProperties tenantMsgProps,
            UserMsgProperties userMsgProps,
            PointToPointMsgQueue msgQueue,
            UserService userService) {
        this.tenantMsgProps = tenantMsgProps;
        this.userMsgProps = userMsgProps;
        this.msgQueue = msgQueue;
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        subscribeToTenantCreatedEvents();
        subscribeToTenantDeletedEvents();
    }

    private void subscribeToTenantCreatedEvents() {
        var topic = tenantMsgProps.getTopics().getTenantCreated().getTopicName();
        var consumerGroup = firstNonBlank(
                tenantMsgProps.getTopics().getTenantCreated().getConsumerGroup(),
                tenantMsgProps.getConsumerGroup());
        Predicate<AppAuthentication> accessCheck = (auth) ->
                auth.isAuthenticated() && auth.getAuthorityNames().contains(SA);
        msgQueue.<TenantCreatedMsg>subscribe(topic, consumerGroup, accessCheck, msg -> {
            // create tenant admin user
            var tenantUuid = msg.getData().getUuid();
            var adminEmail = msg.getData().getAdminEmail();
            var createdUser = userService.create(tenantUuid, User.builder()
                    .username(adminEmail)
                    .displayName("admin")
                    .email(adminEmail)
                    .profileIcon("admin")
                    .build());

            // publish user created event
            var topicName = userMsgProps.getTopics().getUserCreated().getTopicName();
            var userCreatedMsg = UserCreatedMsg.builder()
                    .tenantUuid(tenantUuid)
                    .userUuid(createdUser.getUuid())
                    .username(adminEmail)
                    .roles(Set.of(ADMIN.getName()))
                    .build();
            msgQueue.publish(topicName, userCreatedMsg);
        });
    }

    private void subscribeToTenantDeletedEvents() {
        var topic = tenantMsgProps.getTopics().getTenantDeleted().getTopicName();
        var consumerGroup = firstNonBlank(
                tenantMsgProps.getTopics().getTenantCreated().getConsumerGroup(),
                tenantMsgProps.getConsumerGroup());
        Predicate<AppAuthentication> accessCheck = (auth) ->
                auth.isAuthenticated() && auth.getAuthorityNames().contains(SA);
        msgQueue.<TenantDeletedMsg>subscribe(topic, consumerGroup, accessCheck, msg -> {
            var deletedTenantUuid = msg.getData().getUuid();
            userService.deleteAllByTenantUuid(deletedTenantUuid);
        });
    }
}
