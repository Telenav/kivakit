package com.telenav.kivakit.core.time;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Accesses the creation time of an object, such as a resource.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface CreatedAt
{
    /**
     * Returns the time that has elapsed since this object was created
     */
    default Duration age()
    {
        return createdAt().elapsedSince();
    }

    /**
     * Returns the time at which this object was created
     */
    default Time createdAt()
    {
        return unsupported();
    }

    /**
     * Returns true if this object was created after the given one
     */
    default boolean wasCreatedAfter(CreatedAt that)
    {
        return createdAt().isAfter(that.createdAt());
    }

    /**
     * Returns true if this object was created before the given one
     */
    default boolean wasCreatedBefore(CreatedAt that)
    {
        return createdAt().isBefore(that.createdAt());
    }
}
