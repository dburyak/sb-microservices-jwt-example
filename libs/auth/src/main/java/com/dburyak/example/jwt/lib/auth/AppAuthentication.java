package com.dburyak.example.jwt.lib.auth;

import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.UUID;

/**
 * Specialization of {@link Authentication} for this project. If the project was named "MyVideoPlatform", then
 * this interface would be named "MyVideoPlatformAuthentication".
 */
public interface AppAuthentication extends Authentication {
    UUID getTenantUuid();

    UUID getUserUuid();

    String getDeviceId();

    Set<Role> getRoles();
}
