package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * Identifies whether an object, such as a collection, is empty or non-empty.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@ApiQuality(stability = STABLE,
            testing = UNNECESSARY,
            documentation = SUFFICIENT)
public interface Emptiness
{
    /**
     * @return True if this object contains no values
     */
    boolean isEmpty();

    /**
     * @return True if this object contains one or more values
     */
    default boolean isNonEmpty()
    {
        return !isEmpty();
    }
}
