package com.dburyak.example.jwt.auth.listener;

import com.dburyak.example.jwt.api.internal.tenant.TenantDeletedMsg;
import com.dburyak.example.jwt.api.internal.tenant.cfg.TenantMsgProperties;
import com.dburyak.example.jwt.auth.service.AuthService;
import com.dburyak.example.jwt.auth.service.UserService;
import com.dburyak.example.jwt.lib.auth.AppAuthentication;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static com.dburyak.example.jwt.auth.cfg.Authorities.SA;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;

@Component
@RequiredArgsConstructor
public class TenantMsgListener {
    private final TenantMsgProperties props;
    private final PointToPointMsgQueue msgQueue;
    private final AuthService authService;
    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        subscribeToTenantDeletedEvents();
    }

    private void subscribeToTenantDeletedEvents() {
        var topic = props.getTopics().getTenantDeleted().getTopicName();
        var consumerGroup = firstNonBlank(
                props.getTopics().getTenantCreated().getConsumerGroup(),
                props.getConsumerGroup());
        Predicate<AppAuthentication> accessCheck = (auth) ->
                auth.isAuthenticated() && auth.getAuthorityNames().contains(SA);
        msgQueue.<TenantDeletedMsg>subscribe(topic, consumerGroup, accessCheck, msg -> {
            var deletedTenantUuid = msg.getData().getUuid();
            userService.deleteAllByTenantUuid(deletedTenantUuid);
            authService.deleteAllAuthDataOfTenantUuid(deletedTenantUuid);
        });
    }
}
