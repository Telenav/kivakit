package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * Identifies whether an object, such as a collection, is empty or non-empty.
 */
@FunctionalInterface
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = SUFFICIENT)
public interface Emptiness
{
    boolean isEmpty();

    default boolean isNonEmpty()
    {
        return !isEmpty();
    }
}
