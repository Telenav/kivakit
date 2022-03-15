package com.telenav.kivakit.core.language;

import com.telenav.kivakit.core.object.LazyMap;

import java.util.regex.Pattern;

public class Patterns
{
    private static final LazyMap<String, Pattern> patterns = LazyMap.of(Pattern::compile);

    public static boolean matches(Pattern pattern, String text)
    {
        return pattern.matcher(text).matches();
    }

    public static Pattern simplified(String pattern)
    {
        return patterns.get(pattern
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\*", ".*"));
    }
}
