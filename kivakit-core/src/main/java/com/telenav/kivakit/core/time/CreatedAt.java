package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Accesses the creation time of an object, such as a resource.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
