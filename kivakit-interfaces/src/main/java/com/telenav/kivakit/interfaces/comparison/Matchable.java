package com.telenav.kivakit.interfaces.comparison;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * Interface to code that has a {@link Matcher}, and therefore can be matched
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED)
public interface Matchable<Value>
{
    /**
     * @return The matcher
     */
    Matcher<Value> matcher();

    /**
     * Convenience method that uses this {@link Matchable}'s {@link Matcher} to match against the given value
     *
     * @param value The value to match
     * @return True if the given value matches this {@link Matchable}
     */
    default boolean matches(Value value)
    {
        return matcher().matches(value);
    }
}
