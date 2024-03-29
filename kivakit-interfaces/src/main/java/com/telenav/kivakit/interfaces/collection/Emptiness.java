package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Identifies whether an object, such as a collection, is empty or non-empty.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED,
             reviews = 1,
             reviewers = "shibo")
public interface Emptiness
{
    /**
     * Returns true if this object contains no values
     */
    boolean isEmpty();

    /**
     * Returns true if this object contains one or more values
     */
    default boolean isNonEmpty()
    {
        return !isEmpty();
    }
}
