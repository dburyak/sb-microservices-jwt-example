package com.dburyak.example.jwt.lib.auth.jwt;

import com.dburyak.example.jwt.lib.auth.AuthExtractor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.dburyak.example.jwt.lib.req.Headers.BEARER;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtAuthExtractor implements AuthExtractor {
    private final JwtParser jwtParser;

    @Override
    public JwtAuth extract(Map<String, String> headers) {
        var authHeader = headers.get(AUTHORIZATION);
        if (isBlank(authHeader)) {
            authHeader = headers.get(AUTHORIZATION.toLowerCase());
        }
        if (authHeader != null && authHeader.startsWith(BEARER.getHeader())) {
            var jwtTokenStr = authHeader.substring(7);
            return jwtParser.parse(jwtTokenStr);
        }
        return null;
    }
}
