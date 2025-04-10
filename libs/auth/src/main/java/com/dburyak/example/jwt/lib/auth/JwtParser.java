package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class JwtParser {
    private final io.jsonwebtoken.JwtParser jjwtParser;
    private final Set<String> trustedIssuers;
    private final String rolesKey;
    private final String tenantUuidKey;
    private final String deviceIdKey;

    public JwtParser(JwtAuthProperties props) {
        var jdkKeys = props.getKeys().entrySet().stream()
                .collect(toMap(
                        e -> e.getKey(),
                        e -> e.getValue().toJdkKey()
                ));
        this.jjwtParser = Jwts.parser()
                .keyLocator(new ByKidKeyLocator(jdkKeys))
                .build();
        this.trustedIssuers = props.getTrustedIssuers();
        this.rolesKey = props.getRolesKey();
        this.tenantUuidKey = props.getTenantUuidKey();
        this.deviceIdKey = props.getDeviceIdKey();
    }

    public JwtAuth parse(String jwtStr) {
        // not much sense to bother with signaling different types of failures, filter will always just drop invalid
        // tokens regardless of the exact reason
        Jws<Claims> jwt;
        try {
            jwt = jjwtParser.parseClaimsJws(jwtStr);
        } catch (Exception anyParsingFailure) {
            return null;
        }
        var claims = jwt.getBody();
        var issuer = claims.getIssuer();
        if (!trustedIssuers.contains(issuer)) {
            return null;
        }
        var tenantUuidStr = claims.get(tenantUuidKey, String.class);
        var userUuidStr = claims.getSubject();
        var deviceId = claims.get(deviceIdKey, String.class);
        if (isBlank(tenantUuidStr) || isBlank(userUuidStr) || isBlank(deviceId)) {
            return null;
        }
        var tenantUuid = UUID.fromString(tenantUuidStr);
        var userUuid = UUID.fromString(userUuidStr);
        var rolesRaw = claims.get(rolesKey, ArrayList.class);
        var roles = new HashSet<Role>();
        for (var roleRaw : rolesRaw) {
            roles.add(Role.byName(roleRaw.toString()));
        }
        return new JwtAuth(tenantUuid, userUuid, deviceId, Set.copyOf(roles));
    }
}
