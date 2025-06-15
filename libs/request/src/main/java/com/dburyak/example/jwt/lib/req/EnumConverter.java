package com.dburyak.example.jwt.lib.req;

import org.springframework.core.convert.converter.Converter;

public interface EnumConverter<T extends Enum<T>> extends Converter<String, T> {
    // Same as Converter<String, T> but with a separate type, so we can automate its registration
}
