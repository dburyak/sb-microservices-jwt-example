package com.dburyak.example.jwt.lib.req;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static com.dburyak.example.jwt.lib.req.Attributes.AUTH_TOKEN;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;
import static com.dburyak.example.jwt.lib.req.Attributes.USER_UUID;

public class RequestUtil {

    public String getAuthToken() {
        return getRequestAttr(AUTH_TOKEN);
    }

    public String getAuthToken(HttpServletRequest req) {
        return getRequestAttr(req, AUTH_TOKEN);
    }

    public void setAuthToken(String authToken) {
        putRequestAttr(AUTH_TOKEN, authToken);
    }

    public void setAuthToken(HttpServletRequest req, String authToken) {
        putRequestAttr(req, AUTH_TOKEN, authToken);
    }

    public UUID getTenantUuid() {
        return getRequestAttr(TENANT_UUID);
    }

    public UUID getTenantUuid(HttpServletRequest req) {
        return getRequestAttr(req, TENANT_UUID);
    }

    public void setTenantUuid(UUID tenantUuid) {
        putRequestAttr(TENANT_UUID, tenantUuid);
    }

    public void setTenantUuid(HttpServletRequest req, UUID tenantUuid) {
        putRequestAttr(req, TENANT_UUID, tenantUuid);
    }

    public UUID getUserUuid() {
        return getRequestAttr(USER_UUID);
    }

    public UUID getUserUuid(HttpServletRequest req) {
        return getRequestAttr(req, USER_UUID);
    }

    public void setUserUuid(UUID userUuid) {
        putRequestAttr(USER_UUID, userUuid);
    }

    public void setUserUuid(HttpServletRequest req, UUID userUuid) {
        putRequestAttr(req, USER_UUID, userUuid);
    }

    public String getDeviceId() {
        return getRequestAttr(Attributes.DEVICE_ID);
    }

    public String getDeviceId(HttpServletRequest req) {
        return getRequestAttr(req, Attributes.DEVICE_ID);
    }

    public void setDeviceId(String deviceId) {
        putRequestAttr(Attributes.DEVICE_ID, deviceId);
    }

    public void setDeviceId(HttpServletRequest req, String deviceId) {
        putRequestAttr(req, Attributes.DEVICE_ID, deviceId);
    }

    public boolean isServiceRequest() {
        return Boolean.TRUE.equals(getRequestAttr(Attributes.IS_SERVICE_REQ));
    }

    public boolean isServiceRequest(HttpServletRequest req) {
        return Boolean.TRUE.equals(getRequestAttr(req, Attributes.IS_SERVICE_REQ));
    }

    public void setServiceRequest(boolean isServiceRequest) {
        putRequestAttr(Attributes.IS_SERVICE_REQ, isServiceRequest);
    }

    public void setServiceRequest(HttpServletRequest req, boolean isServiceRequest) {
        putRequestAttr(req, Attributes.IS_SERVICE_REQ, isServiceRequest);
    }

    public <T> void putRequestAttr(String key, T value) {
        putRequestAttr(currentRequest(), key, value);
    }

    public <T> void putRequestAttr(HttpServletRequest req, String key, T value) {
        req.setAttribute(key, value);
    }

    public <T> T getRequestAttr(String key) {
        return getRequestAttr(currentRequest(), key);
    }

    public <T> T getRequestAttr(HttpServletRequest req, String key) {
        return req != null ? (T) req.getAttribute(key) : null;
    }

    private HttpServletRequest currentRequest() {
        var reqAttr = RequestContextHolder.getRequestAttributes();
        return reqAttr != null ? ((ServletRequestAttributes) reqAttr).getRequest() : null;
    }
}
