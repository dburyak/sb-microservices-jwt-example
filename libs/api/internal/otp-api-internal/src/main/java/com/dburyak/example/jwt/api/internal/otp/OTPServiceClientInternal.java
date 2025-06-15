package com.dburyak.example.jwt.api.internal.otp;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.otp.cfg.OTPServiceClientProperties;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import com.dburyak.example.jwt.lib.req.Headers;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;
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
    private final Optional<ServiceTokenManager> serviceTokenManager;

    public OTPServiceClientInternal(RequestUtil requestUtil, Optional<ServiceTokenManager> serviceTokenManager,
            OTPServiceClientProperties props) {
        this.requestUtil = requestUtil;
        this.serviceTokenManager = serviceTokenManager;
        this.rest = RestClient.builder()
                .baseUrl(props.getUrl())
                .build();
    }

    public RegisteredUserOTP getRegisteredUserOTP(RegisteredUserOTP.Type type) {
        return requestUtil.withPropagatedAuth(rest.get()
                        .uri(u -> u
                                .path(PATH_REGISTERED_OTP_BY_TYPE)
                                .build(type))
                )
                .retrieve()
                .body(RegisteredUserOTP.class);
    }

    public RegisteredUserOTP getExternallyIdentifiedOTP(UUID tenantUuid, String deviceId, RegisteredUserOTP.Type type,
            ExternalId externalId) {
        return requestUtil.withPropagatedAuth(rest.post()
                        .uri(u -> u
                                .path(PATH_ANONYMOUS_OTP_BY_TYPE + GET_RESOURCE)
                                .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                .queryParam(QueryParams.DEVICE_ID, deviceId)
                                .build(type))
                        .body(externalId)
                )
                .retrieve()
                .body(RegisteredUserOTP.class);
    }

    public boolean claimRegisteredUserOTP(RegisteredUserOTP.Type type, String code) {
        var resp = requestUtil.withPropagatedAuth(rest.delete()
                        .uri(u -> u
                                .path(PATH_REGISTERED_OTP_BY_CODE)
                                .build(type, code))
                )
                .retrieve()
                .toBodilessEntity();
        return resp.getStatusCode().is2xxSuccessful();
    }

    public boolean claimExternallyIdentifiedOTP(UUID tenantUuid, String deviceId, ExternallyIdentifiedOTP.Type type,
            String code, ExternalId externalId) {
        try {
            var resp = requestUtil.withPropagatedAuthOrToken(
                            () -> serviceTokenManager.map(ServiceTokenManager::getServiceToken).orElseThrow(),
                            rest.post()
                                    .uri(u -> u
                                            .path(PATH_ANONYMOUS_OTP_BY_CODE + DELETE_RESOURCE)
                                            .queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                            .queryParam(QueryParams.DEVICE_ID, deviceId)
                                            .build(type, code))
                                    .body(externalId)
                    )
                    .retrieve()
                    .toBodilessEntity();
            return resp.getStatusCode().is2xxSuccessful();
        } catch (RestClientResponseException err) {
            if (err.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
                // OTP not found, return false
                return false;
            }
            // in real world app, other errors/statuses might trip circuit breaker
            return false;
        }
    }
}
