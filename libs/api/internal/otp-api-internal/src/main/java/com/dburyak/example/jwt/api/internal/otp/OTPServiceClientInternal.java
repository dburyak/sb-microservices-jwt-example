package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPServiceClientProperties;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static com.dburyak.example.jwt.api.common.Paths.DELETE_RESOURCE;
import static com.dburyak.example.jwt.api.common.Paths.GET_RESOURCE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_ANONYMOUS_OTP_BY_CODE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_ANONYMOUS_OTP_BY_TYPE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_REGISTERED_OTP_BY_CODE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_REGISTERED_OTP_BY_TYPE;

public class OTPServiceClientInternal {
    private final RequestUtil requestUtil;
    private final RestClient rest;

    public OTPServiceClientInternal(RequestUtil requestUtil, OTPServiceClientProperties props) {
        this.requestUtil = requestUtil;
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
    }

    public RegisteredUserOTP getRegisteredUserOTP(UUID userUuid, String deviceId, RegisteredUserOTP.Type type) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_REGISTERED_OTP_BY_TYPE)
                                .build(userUuid, deviceId, type))
                )
                .retrieve()
                .body(RegisteredUserOTP.class);
    }

    public RegisteredUserOTP getRegisteredUserOTP(RegisteredUserOTP.Type type) {
        var userUuid = requestUtil.getUserUuid();
        var deviceId = requestUtil.getDeviceId();
        return getRegisteredUserOTP(userUuid, deviceId, type);
    }

    public RegisteredUserOTP getExternallyIdentifiedOTP(UUID tenantUuid, String deviceId, RegisteredUserOTP.Type type,
            ExternalId externalId) {
        return requestUtil.withPropagatedAuth(rest.post()
                        .uri(u -> u
                                .path(PATH_ANONYMOUS_OTP_BY_TYPE + GET_RESOURCE)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                .build(deviceId, type))
                        .body(externalId)
                )
                .retrieve()
                .body(RegisteredUserOTP.class);
    }

    public boolean claimRegisteredUserOTP(UUID userUuid, String deviceId, RegisteredUserOTP.Type type,
            String code) {
        var resp = requestUtil.withPropagatedAuth(rest.delete()
                        .uri(u -> u
                                .path(PATH_REGISTERED_OTP_BY_CODE)
                                .build(userUuid, deviceId, type, code))
                )
                .retrieve()
                .toBodilessEntity();
        return resp.getStatusCode().is2xxSuccessful();
    }

    public boolean claimRegisteredUserOTP(RegisteredUserOTP.Type type, String code) {
        var userUuid = requestUtil.getUserUuid();
        var deviceId = requestUtil.getDeviceId();
        return claimRegisteredUserOTP(userUuid, deviceId, type, code);
    }

    public boolean claimExternallyIdentifiedOTP(UUID tenantUuid, UUID userUuid, String deviceId,
            ExternallyIdentifiedOTP.Type type, String code) {
        var resp = requestUtil.withPropagatedAuth(rest.post()
                        .uri(u -> u
                                .path(PATH_ANONYMOUS_OTP_BY_CODE + DELETE_RESOURCE)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                .build(deviceId, type, code))
                )
                .retrieve()
                .toBodilessEntity();
        return resp.getStatusCode().is2xxSuccessful();
    }
}
