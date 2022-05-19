package com.telenav.kivakit.interfaces.comparison;

/**
 * Interface to code that has a {@link Matcher}
 *
 * @author jonathanl (shibo)
 */
public interface Matchable<Value>
{
    /**
     * @return The matcher
     */
    Matcher<Value> matcher();

    default boolean matches(Value value)
    {
        return matcher().matches(value);
    }
}
