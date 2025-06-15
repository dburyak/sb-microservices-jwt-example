package com.dburyak.example.jwt.lib.req;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static com.dburyak.example.jwt.lib.req.Attributes.AUTH_TOKEN;
import static com.dburyak.example.jwt.lib.req.Attributes.IS_SERVICE_REQ;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;
import static com.dburyak.example.jwt.lib.req.Attributes.USER_UUID;
import static com.dburyak.example.jwt.lib.req.Headers.API_KEY;
import static com.dburyak.example.jwt.lib.req.Headers.BEARER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class RequestUtil {
    // thread local map to simulate request attributes for messaging
    private final ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(HashMap::new);

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

    public void clearAuthToken() {
        clearRequestAttr(AUTH_TOKEN);
    }

    public void clearAuthToken(HttpServletRequest req) {
        clearRequestAttr(req, AUTH_TOKEN);
    }

    public String getApiKey() {
        return getRequestAttr(Attributes.API_KEY);
    }

    public String getApiKey(HttpServletRequest req) {
        return getRequestAttr(req, Attributes.API_KEY);
    }

    public void setApiKey(String apiKey) {
        putRequestAttr(Attributes.API_KEY, apiKey);
    }

    public void setApiKey(HttpServletRequest req, String apiKey) {
        putRequestAttr(req, Attributes.API_KEY, apiKey);
    }

    public void clearApiKey() {
        clearRequestAttr(Attributes.API_KEY);
    }

    public void clearApiKey(HttpServletRequest req) {
        clearRequestAttr(req, Attributes.API_KEY);
    }

    public UUID getCallersTenantUuid() {
        return getRequestAttr(Attributes.CALLERS_TENANT_UUID);
    }

    public UUID getCallersTenantUuid(HttpServletRequest req) {
        return getRequestAttr(req, Attributes.CALLERS_TENANT_UUID);
    }

    public void setCallersTenantUuid(UUID callersTenantUuid) {
        putRequestAttr(Attributes.CALLERS_TENANT_UUID, callersTenantUuid);
    }

    public void setCallersTenantUuid(HttpServletRequest req, UUID callersTenantUuid) {
        putRequestAttr(req, Attributes.CALLERS_TENANT_UUID, callersTenantUuid);
    }

    public void clearCallersTenantUuid() {
        clearRequestAttr(Attributes.CALLERS_TENANT_UUID);
    }

    public void clearCallersTenantUuid(HttpServletRequest req) {
        clearRequestAttr(req, Attributes.CALLERS_TENANT_UUID);
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

    public void clearTenantUuid() {
        clearRequestAttr(TENANT_UUID);
    }

    public void clearTenantUuid(HttpServletRequest req) {
        clearRequestAttr(req, TENANT_UUID);
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

    public void clearUserUuid() {
        clearRequestAttr(USER_UUID);
    }

    public void clearUserUuid(HttpServletRequest req) {
        clearRequestAttr(req, USER_UUID);
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

    public void clearDeviceId() {
        clearRequestAttr(Attributes.DEVICE_ID);
    }

    public void clearDeviceId(HttpServletRequest req) {
        clearRequestAttr(req, Attributes.DEVICE_ID);
    }

    public boolean isServiceRequest() {
        return Boolean.TRUE.equals(getRequestAttr(IS_SERVICE_REQ));
    }

    public boolean isServiceRequest(HttpServletRequest req) {
        return Boolean.TRUE.equals(getRequestAttr(req, IS_SERVICE_REQ));
    }

    public void setServiceRequest(boolean isServiceRequest) {
        putRequestAttr(IS_SERVICE_REQ, isServiceRequest);
    }

    public void setServiceRequest(HttpServletRequest req, boolean isServiceRequest) {
        putRequestAttr(req, IS_SERVICE_REQ, isServiceRequest);
    }

    public void clearServiceRequest() {
        clearRequestAttr(IS_SERVICE_REQ);
    }

    public void clearServiceRequest(HttpServletRequest req) {
        clearRequestAttr(req, IS_SERVICE_REQ);
    }

    public <T> void putRequestAttr(String key, T value) {
        putRequestAttr(getCurrentHttpRequest(), key, value);
    }

    public <T> void putRequestAttr(HttpServletRequest req, String key, T value) {
        if (req != null) {
            req.setAttribute(key, value);
        } else {
            threadLocal.get().put(key, value);
        }
    }

    public <T> T getRequestAttr(String key) {
        return getRequestAttr(getCurrentHttpRequest(), key);
    }

    public <T> T getRequestAttr(HttpServletRequest req, String key) {
        return req != null ? (T) req.getAttribute(key) : (T) threadLocal.get().get(key);
    }

    public void clearRequestAttr(String key) {
        clearRequestAttr(getCurrentHttpRequest(), key);
    }

    public void clearRequestAttr(HttpServletRequest req, String key) {
        if (req != null) {
            req.removeAttribute(key);
        } else {
            threadLocal.get().remove(key);
        }
    }

    public HttpServletRequest getCurrentHttpRequest() {
        var reqAttr = RequestContextHolder.getRequestAttributes();
        return reqAttr != null ? ((ServletRequestAttributes) reqAttr).getRequest() : null;
    }

    public <T extends RequestHeadersSpec<T>> RequestHeadersSpec<T> withPropagatedAuth(RequestHeadersSpec<T> spec) {
        var withAuth = withPropagatedAuthInternal(spec);
        if (withAuth == null) {
            throw new IllegalStateException("no auth info found in request ctx");
        }
        return withAuth;
    }

    public <T extends RequestHeadersSpec<T>> RequestHeadersSpec<T> withPropagatedAuthOrToken(
            Supplier<String> tokenProvider, RequestHeadersSpec<T> spec) {
        var withAuth = withPropagatedAuthInternal(spec);
        if (withAuth != null) {
            return withAuth;
        }
        var authToken = tokenProvider.get();
        if (isNotBlank(authToken)) {
            return spec.header(AUTHORIZATION, BEARER + authToken);
        } else {
            throw new IllegalStateException("no auth info found in request ctx and no token provided");
        }
    }

    public <T extends RequestHeadersSpec<T>> RequestHeadersSpec<T> withPropagatedAuthOrApiKey(
            Supplier<String> apiKeyProvider, RequestHeadersSpec<T> spec) {
        var withAuth = withPropagatedAuthInternal(spec);
        if (withAuth != null) {
            return withAuth;
        }
        var key = apiKeyProvider.get();
        if (isNotBlank(key)) {
            return spec.header(API_KEY.getHeader(), key);
        } else {
            throw new IllegalStateException("no auth info found in request ctx and no API key provided");
        }
    }

    private <T extends RequestHeadersSpec<T>> RequestHeadersSpec<T> withPropagatedAuthInternal(
            RequestHeadersSpec<T> spec) {
        var authToken = getAuthToken();
        if (isNotBlank(authToken)) {
            return spec.header(AUTHORIZATION, BEARER + authToken);
        }
        var apiKey = getApiKey();
        if (isNotBlank(apiKey)) {
            return spec.header(API_KEY.getHeader(), apiKey);
        }
        return null;
    }
}
