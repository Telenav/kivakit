package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Accesses the creation time of an object, such as a resource.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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
