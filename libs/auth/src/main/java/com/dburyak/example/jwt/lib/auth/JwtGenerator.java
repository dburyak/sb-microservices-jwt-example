package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;
import com.dburyak.example.jwt.lib.req.ReservedIdentifiers;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_DEVICE_ID;
import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SERVICE_TENANT_UUID;
import static java.util.Collections.emptySet;

public class JwtGenerator {
    private final String issuer;
    private final Duration ttl;
    private final String rolesKey;
    private final Key key;
    private final String keyId;
    private final String tenantIdKey;
    private final String deviceIdKey;
    private final String serviceTokenIssuer;

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
        this.serviceTokenIssuer = StringUtils.isNotBlank(props.getServiceToken().getIssuer())
                ? props.getServiceToken().getIssuer()
                : props.getGenerator().getIssuer();
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
    public String generateUserToken(UUID tenantId, UUID userUuid, String deviceId, Set<String> roles) {
        return generateInternal(tenantId, userUuid, deviceId, roles, issuer);
    }

    /**
     * Generate JWT token for service.
     *
     * @param serviceUuid special service uuid, one of {@link ReservedIdentifiers}
     * @param roles roles of the service
     *
     * @return JWT token
     */
    public String generateServiceToken(UUID serviceUuid, Set<String> roles) {
        return generateInternal(SERVICE_TENANT_UUID, serviceUuid, SERVICE_DEVICE_ID, roles, serviceTokenIssuer);
    }

    private String generateInternal(UUID tenantId, UUID userUuid, String deviceId, Set<String> roles, String issuer) {
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
                .claim(rolesKey, roles != null ? roles : emptySet())
                .signWith(key)
                .compact();
    }
}
