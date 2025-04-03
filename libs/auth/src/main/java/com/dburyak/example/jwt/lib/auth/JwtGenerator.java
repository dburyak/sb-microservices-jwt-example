package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

public class JwtGenerator {
    private final String issuer;
    private final Duration ttl;
    private final String rolesKey;
    private final Key key;
    private final String keyId;

    public JwtGenerator(JwtAuthProperties props) {
        if (!props.getGenerator().isEnabled()) {
            throw new IllegalStateException("JWT generator should be enabled to use it");
        }
        this.issuer = props.getGenerator().getIssuer();
        this.ttl = props.getGenerator().getTtl();
        this.rolesKey = props.getRolesKey();
        this.key = props.getGenerator().getKey().toJdkKey();
        this.keyId = props.getGenerator().getKey().getKid();
    }

    /**
     * Generate JWT token.
     *
     * @param subject subject of the JWT token - user-id or service-name if token is generated for service
     * @param roles roles of the subject
     *
     * @return JWT token
     */
    public String generate(String tenantId, String subject, Set<String> roles) {
        var now = Instant.now();
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .keyId(keyId)
                .and()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .claim(rolesKey, roles)
                .signWith(key)
                .compact();
    }
}
