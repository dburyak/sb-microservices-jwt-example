package com.dburyak.example.jwt.auth.cfg;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Authorities {
    public static final String USER_READ = "USER_READ";
    public static final String USER_WRITE = "USER_WRITE";
    public static final String USER_ALLT_READ = "USER_ALLT_READ"; // ALLT = all tenants
    public static final String USER_ALLT_WRITE = "USER_ALLT_WRITE"; // ALLT = all tenants
    public static final String USER_ADM_READ = "USER_ADM_READ";
    public static final String USER_ADM_WRITE = "USER_ADM_WRITE";
}
