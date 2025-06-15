package com.dburyak.example.jwt.lib.req;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class AbstractEnumConverter<T extends Enum<T>> implements EnumConverter<T> {
    private static final Pattern PATTERN_WORDS = Pattern.compile("[A-Z]+");
    private final Class<T> enumType;

    public AbstractEnumConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public final T convert(String str) {
        if (isBlank(str)) {
            return null;
        }
        var matcher = PATTERN_WORDS.matcher(str);
        var start = 0;
        var pos = 0;
        var words = new ArrayList<String>();
        while (matcher.find()) {
            pos = matcher.start();
            var word = str.substring(start, pos - 1);
            start = pos;
            words.add(word);
        }
        if (start < str.length()) {
            words.add(str.substring(start));
        }
        var enumName = String.join("_", words)
                .replace('-', '_')
                .toUpperCase();
        return Enum.valueOf(enumType, enumName);
    }
}
