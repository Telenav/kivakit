package com.telenav.kivakit.kernel.interfaces.collection;

/**
 * Interface to an object which can contain a value or not
 *
 * @author jonathanl (shibo)
 */
public interface Presence
{
    /**
     * Returns true if this object's value is missing
     */
    boolean isAbsent();

    /**
     * Returns true if this object has a value
     */
    default boolean isPresent()
    {
        return !isAbsent();
    }
}
