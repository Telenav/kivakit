package com.telenav.kivakit.core.language;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.object.LazyMap;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_STATIC_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Utility methods for working with regular expression {@link Pattern}s.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_STATIC_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
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
