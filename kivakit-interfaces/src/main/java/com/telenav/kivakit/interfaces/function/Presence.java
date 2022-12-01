package com.telenav.kivakit.interfaces.function;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to an object which can contain a value or not
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
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
