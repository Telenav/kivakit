package com.telenav.kivakit.interfaces.comparison;

/**
 * Interface to code that has a {@link Matcher}
 *
 * @author jonathanl (shibo)
 */
public interface Matchable<T>
{
    /**
     * @return The matcher
     */
    Matcher<T> matcher();
}
