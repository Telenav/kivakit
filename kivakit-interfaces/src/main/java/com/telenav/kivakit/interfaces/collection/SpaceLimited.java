package com.telenav.kivakit.interfaces.collection;

/**
 * An object that can store a limited number of values
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface SpaceLimited
{
    /**
     * @param values The number of values desired to add
     * @return True if the given number of values can be added
     */
    default boolean hasRoomFor(int values)
    {
        return true;
    }
}
