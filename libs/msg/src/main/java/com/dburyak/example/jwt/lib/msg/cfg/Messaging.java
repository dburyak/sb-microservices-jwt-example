package com.dburyak.example.jwt.lib.msg.cfg;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
public @interface Messaging {
}
