package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.internal.otp.cfg.OTPServiceClientProperties;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.web.client.RestClient;

import java.util.UUID;

public class OTPServiceClient {
    private final RequestUtil requestUtil;
    private final RestClient rest;

    public OTPServiceClient(RequestUtil requestUtil, OTPServiceClientProperties props) {
        this.requestUtil = requestUtil;
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
    }

    public OTP getOTP(UUID tenantUuid, UUID userUuid, String deviceId, OTPType type) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(OTPServiceClientProperties.OTP_ROOT + OTPServiceClientProperties.OTP_BY_UUID)
                                .queryParam(OTPServiceClientProperties.TENANT_UUID, tenantUuid) // in case if it's a service request
                                .queryParam(OTPServiceClientProperties.DEVICE_ID, deviceId)
                                .queryParam(OTPServiceClientProperties.TYPE, type)
                                .build(userUuid))
                )
                .retrieve()
                .body(OTP.class);
    }

}
