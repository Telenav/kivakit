package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.testing.NoTestRequired;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Accesses the creation time of an object, such as a resource.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@NoTestRequired
public interface CreatedAt
{
    /**
     * Returns the time that has elapsed since this object was created
     */
    @NoTestRequired
    default Duration age()
    {
        return createdAt().elapsedSince();
    }

    /**
     * Returns the time at which this object was created
     */
    @NoTestRequired
    default Time createdAt()
    {
        return unsupported();
    }

    /**
     * Returns true if this object was created after the given one
     */
    @NoTestRequired
    default boolean wasCreatedAfter(CreatedAt that)
    {
        return createdAt().isAfter(that.createdAt());
    }

    /**
     * Returns true if this object was created before the given one
     */
    @NoTestRequired
    default boolean wasCreatedBefore(CreatedAt that)
    {
        return createdAt().isBefore(that.createdAt());
    }
}
