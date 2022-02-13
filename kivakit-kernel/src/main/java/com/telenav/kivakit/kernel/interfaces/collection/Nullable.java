package com.telenav.kivakit.kernel.interfaces.collection;

/**
 * Interface to an object which can contain a value or null
 *
 * @author jonathanl (shibo)
 */
public interface Nullable
{
    /**
     * Checks this object for null
     *
     * @return True if this object's value is null
     */
    boolean isEmpty();

    /**
     * Checks this object for non-null
     *
     * @return True if this object's value is not null
     */
    default boolean isPresent()
    {
        return !isEmpty();
    }
}
