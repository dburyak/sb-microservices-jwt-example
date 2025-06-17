package com.dburyak.example.jwt.lib.auth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StandardAuthorities {
    public static final String SA = "SA"; // super-admin of the whole system
    public static final String ADMIN = "ADMIN"; // admin of own tenant only
    public static final String ACTUATOR_READ = "ACTUATOR_READ";
}
