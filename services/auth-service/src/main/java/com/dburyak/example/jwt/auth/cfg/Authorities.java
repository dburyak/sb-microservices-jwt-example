package com.dburyak.example.jwt.auth.cfg;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Authorities {
    public static final String USER_READ = "USER_READ";
    public static final String USER_WRITE = "USER_WRITE";
    public static final String USER_ALL_READ = "USER_ALL_READ";
    public static final String USER_ALL_WRITE = "USER_ALL_WRITE";
}
