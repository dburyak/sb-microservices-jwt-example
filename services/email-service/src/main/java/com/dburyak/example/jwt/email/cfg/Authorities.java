package com.dburyak.example.jwt.email.cfg;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Authorities {
    // in a more complex scenario, there may be more fine-grained authorities
    // like SEND_PASSWORD_RESET_EMAIL, SEND_MARKETING_EMAIL, etc.
    public static final String SEND_EMAIL = "SEND_EMAIL";
}
