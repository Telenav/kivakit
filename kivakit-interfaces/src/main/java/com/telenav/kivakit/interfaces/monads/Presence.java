package com.telenav.kivakit.interfaces.monads;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Interface to an object which can contain a value or not
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
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
