package com.dburyak.example.jwt.lib.auth.apikey;

import com.dburyak.example.jwt.lib.auth.AuthExtractor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.dburyak.example.jwt.lib.req.Headers.API_KEY;
import static org.apache.commons.lang3.StringUtils.isBlank;

@RequiredArgsConstructor
public class ApiKeyAuthExtractor implements AuthExtractor {

    @Override
    public ApiKeyAuth extract(Map<String, String> headers) {
        var apiKey = headers.get(API_KEY.getHeader());
        if (isBlank(apiKey)) {
            apiKey = headers.get(API_KEY.getHeader());
            return new ApiKeyAuth(apiKey);
        }
        return null;
    }
}
