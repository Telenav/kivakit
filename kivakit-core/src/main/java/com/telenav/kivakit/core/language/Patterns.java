package com.telenav.kivakit.core.language;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.object.LazyMap;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.object.LazyMap.lazyMap;

/**
 * Utility methods for working with regular expression {@link Pattern}s.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Patterns
{
    private static final LazyMap<String, Pattern> patterns = lazyMap(Pattern::compile);

    /**
     * Matches the given text against the given pattern
     */
    public static boolean patternMatches(Pattern pattern, String text)
    {
        return pattern.matcher(text).matches();
    }

    /**
     * Transforms the given pattern string into a simplified pattern, where "." means a literal dot, and "*" means ".*"
     * (anything but newline)
     */
    public static Pattern simplifiedPattern(String pattern)
    {
        return patterns.get(pattern
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\*", ".*"));
    }
}
