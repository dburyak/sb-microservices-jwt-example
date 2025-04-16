package com.dburyak.example.jwt.lib.auth;

import java.util.Map;

/**
 * Extracts authentication information from the headers. Generalized to be usable both in http request filters, and
 * in message consumers.
 */
public interface AuthExtractor {
    AppAuthentication extract(Map<String, String> headers);
}
