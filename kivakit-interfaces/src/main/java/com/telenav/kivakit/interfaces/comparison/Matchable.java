package com.telenav.kivakit.interfaces.comparison;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to code that has a {@link Matcher}, and therefore can be matched
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
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
