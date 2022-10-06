package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Interface for iteration where hasNext() is replaced by the semantics that null represents the end of iteration
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public interface NextIterator<Value>
{
    /**
     * @return The next Value or null if there is none
     */
    Value next();
}
