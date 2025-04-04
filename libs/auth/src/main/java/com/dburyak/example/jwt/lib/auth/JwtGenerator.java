package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class JwtGenerator {
    private final String issuer;
    private final Duration ttl;
    private final String rolesKey;
    private final Key key;
    private final String keyId;
    private final String tenantIdKey;
    private final String deviceIdKey;

    public JwtGenerator(JwtAuthProperties props) {
        if (!props.getGenerator().isEnabled()) {
            throw new IllegalStateException("JWT generator should be enabled to use it");
        }
        this.issuer = props.getGenerator().getIssuer();
        this.ttl = props.getGenerator().getTtl();
        this.rolesKey = props.getRolesKey();
        this.key = props.getGenerator().getKey().toJdkKey();
        this.keyId = props.getGenerator().getKey().getKid();
        this.tenantIdKey = props.getTenantIdKey();
        this.deviceIdKey = props.getDeviceIdKey();
    }

    /**
     * Generate JWT token for user.
     *
     * @param tenantId tenantId tenant id
     * @param userUuid userUuid user unique identifier
     * @param deviceId device id
     * @param roles roles of the user
     *
     * @return JWT token
     */
    public String generateUserToken(String tenantId, UUID userUuid, String deviceId, Set<String> roles) {
        var now = Instant.now();
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .keyId(keyId)
                .and()
                .issuer(issuer)
                .subject(userUuid.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .claim(tenantIdKey, tenantId)
                .claim(deviceIdKey, deviceId)
                .claim(rolesKey, roles)
                .signWith(key)
                .compact();
    }

    /**
     * Generate JWT token for service.
     *
     * @param service service name
     * @param roles roles of the service
     *
     * @return JWT token
     */
    public String generateServiceToken(String service, Set<String> roles) {
        var now = Instant.now();
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .keyId(keyId)
                .and()
                .issuer(issuer)
                .subject(service)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .claim(rolesKey, roles)
                .signWith(key)
                .compact();
    }
}
