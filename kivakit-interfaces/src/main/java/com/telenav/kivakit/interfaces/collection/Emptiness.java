package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Identifies whether an object, such as a collection, is empty or non-empty.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
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
