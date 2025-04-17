package com.dburyak.example.jwt.auth.controller;

import com.dburyak.example.jwt.api.internal.tenant.cfg.TenantServiceClientProperties;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantMsgListener {
    private final TenantServiceClientProperties props;
    private final PointToPointMsgQueue queue;

    @EventListener(ApplicationReadyEvent.class)
    public void startMsgProcessing() {
        queue.subscribe();
    }
}
