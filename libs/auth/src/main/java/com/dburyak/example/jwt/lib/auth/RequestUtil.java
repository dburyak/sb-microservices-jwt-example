package com.dburyak.example.jwt.lib.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static com.dburyak.example.jwt.lib.auth.Attributes.AUTH_TOKEN;
import static com.dburyak.example.jwt.lib.auth.Attributes.TENANT_ID;
import static com.dburyak.example.jwt.lib.auth.Attributes.USER_UUID;

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

    public String getTenantId() {
        return getRequestAttr(TENANT_ID);
    }

    public String getTenantId(HttpServletRequest req) {
        return getRequestAttr(req, TENANT_ID);
    }

    public void setTenantId(String tenantId) {
        putRequestAttr(TENANT_ID, tenantId);
    }

    public void setTenantId(HttpServletRequest req, String tenantId) {
        putRequestAttr(req, TENANT_ID, tenantId);
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
        return (T) req.getAttribute(key);
    }

    private HttpServletRequest currentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
