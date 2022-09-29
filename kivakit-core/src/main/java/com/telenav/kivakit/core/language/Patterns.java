package com.telenav.kivakit.core.language;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.object.LazyMap;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Utility methods for working with regular expression {@link Pattern}s.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_STATIC_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Patterns
{
    private static final LazyMap<String, Pattern> patterns = LazyMap.of(Pattern::compile);

    /**
     * Matches the given text against the given pattern
     */
    public static boolean matches(Pattern pattern, String text)
    {
        return pattern.matcher(text).matches();
    }

    /**
     * Transforms the given pattern string into a simplified pattern, where "." means a literal dot, and "*" means ".*"
     * (anything but newline)
     */
    public static Pattern simplified(String pattern)
    {
        return patterns.get(pattern
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\*", ".*"));
    }
}
