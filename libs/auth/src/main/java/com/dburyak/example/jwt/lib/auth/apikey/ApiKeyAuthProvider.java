package com.dburyak.example.jwt.lib.auth.apikey;

import com.dburyak.example.jwt.api.internal.auth.AuthServiceClientInternal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiKeyAuthProvider {
    private final AuthServiceClientInternal authServiceClient;

    // TODO: implement, make a call to auth-service
}
