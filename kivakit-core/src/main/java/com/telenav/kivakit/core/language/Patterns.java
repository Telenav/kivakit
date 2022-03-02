package com.telenav.kivakit.core.language;

import java.util.regex.Pattern;

public class Patterns
{
    public static Pattern simplified(String pattern)
    {
        return Pattern.compile(pattern
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\*", ".*"));
    }
}
