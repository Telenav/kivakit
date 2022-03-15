package com.telenav.kivakit.interfaces.string;

/**
 * Interface to code that maps from a string to a value
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface StringMapper<Value>
{
    /**
     * Maps the given text to a value
     *
     * @param text The text
     * @return The value
     */
    Value map(String text);
}
