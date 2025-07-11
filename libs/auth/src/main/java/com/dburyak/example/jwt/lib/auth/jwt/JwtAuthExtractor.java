package com.dburyak.example.jwt.lib.auth.jwt;

import com.dburyak.example.jwt.lib.auth.AuthExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import java.util.Map;

import static com.dburyak.example.jwt.lib.req.Headers.AUTHORIZATION;
import static com.dburyak.example.jwt.lib.req.Headers.BEARER;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Order(0)
@RequiredArgsConstructor
public class JwtAuthExtractor implements AuthExtractor {
    private final JwtParser jwtParser;

    @Override
    public JwtAuth extract(Map<String, String> headers) {
        var authHeader = headers.get(AUTHORIZATION.getHeader());
        if (isBlank(authHeader)) {
            authHeader = headers.get(AUTHORIZATION.getHeader().toLowerCase());
        }
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            var jwtTokenStr = authHeader.substring(7);
            return jwtParser.parse(jwtTokenStr);
        }
        return null;
    }
}
