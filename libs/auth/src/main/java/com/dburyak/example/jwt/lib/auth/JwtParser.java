package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class JwtParser {
    private final io.jsonwebtoken.JwtParser jjwtParser;
    private final Set<String> trustedIssuers;
    private final String rolesKey;
    private final String tenantIdKey;

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
        this.tenantIdKey = props.getTenantIdKey();
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
        var userId = claims.getSubject();
        var tenantId = claims.get(tenantIdKey, String.class);
        if (isBlank(userId) || isBlank(tenantId)) {
            return null;
        }
        var roles = claims.get(rolesKey, Set.class);
        return new JwtAuth(tenantId, userId, roles);
    }
}
