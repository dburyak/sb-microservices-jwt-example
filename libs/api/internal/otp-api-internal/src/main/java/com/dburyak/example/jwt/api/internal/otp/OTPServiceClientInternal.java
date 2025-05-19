package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPServiceClientProperties;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_OTP_BY_CODE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_OTP_BY_TYPE;

public class OTPServiceClientInternal {
    private final RequestUtil requestUtil;
    private final RestClient rest;

    public OTPServiceClientInternal(RequestUtil requestUtil, OTPServiceClientProperties props) {
        this.requestUtil = requestUtil;
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
    }

    public OTP getOTPByType(UUID userUuid, String deviceId, OTPType type) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_OTP_BY_TYPE)
                                .build(userUuid, deviceId, type))
                )
                .retrieve()
                .body(OTP.class);
    }

    public OTP getOTPByType(UUID tenantUuid, UUID userUuid, String deviceId, OTPType type) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_OTP_BY_TYPE)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                .build(userUuid, deviceId, type))
                )
                .retrieve()
                .body(OTP.class);
    }

    public OTP getOTPByTypeAndCode(UUID userUuid, String deviceId, OTPType type, String code) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_OTP_BY_CODE)
                                .build(userUuid, deviceId, type, code))
                )
                .retrieve()
                .body(OTP.class);
    }

    public OTP getOTPByTypeAndCode(UUID tenantUuid, UUID userUuid, String deviceId, OTPType type, String code) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_OTP_BY_CODE)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                .build(userUuid, deviceId, type, code))
                )
                .retrieve()
                .body(OTP.class);
    }

    public OTP claimOTPByTypeAndCode(UUID userUuid, String deviceId, OTPType type, String code) {
        return requestUtil.withPropagatedAuth(rest.delete()
                        .uri(u -> u
                                .path(PATH_OTP_BY_CODE)
                                .build(userUuid, deviceId, type, code))
                )
                .retrieve()
                .body(OTP.class);
    }

    public OTP claimOTPByTypeAndCode(UUID tenantUuid, UUID userUuid, String deviceId, OTPType type, String code) {
        return requestUtil.withPropagatedAuth(rest.delete()
                        .uri(u -> u
                                .path(PATH_OTP_BY_CODE)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                .build(userUuid, deviceId, type, code))
                )
                .retrieve()
                .body(OTP.class);
    }
}
