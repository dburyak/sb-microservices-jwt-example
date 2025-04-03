package com.dburyak.example.jwt.lib.auth;

import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.ProtectedHeader;
import lombok.RequiredArgsConstructor;

import java.security.Key;
import java.util.Map;

@RequiredArgsConstructor
class ByKidKeyLocator extends LocatorAdapter<Key> {
    private final Map<String, Key> keys;

    @Override
    protected Key locate(ProtectedHeader header) {
        return keys.get(header.getKeyId());
    }
}
