package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * Identifies whether an object, such as a collection, is empty or non-empty.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = DOCUMENTED)
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
