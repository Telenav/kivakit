package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * Identifies whether an object, such as a collection, is empty or non-empty.
 */
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = COMPLETE)
public interface Emptiness
{
    boolean isEmpty();

    default boolean isNonEmpty()
    {
        return !isEmpty();
    }
}
