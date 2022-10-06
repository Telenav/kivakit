package com.telenav.kivakit.interfaces.monads;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Interface to an object which can contain a value or not
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE,
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
