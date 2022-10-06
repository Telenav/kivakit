package com.telenav.kivakit.interfaces.string;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Interface to code that maps from a string to a value
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Parsable<Value>
{
    /**
     * Maps the given text to a value
     *
     * @param text The text
     * @return The value
     */
    Value parse(String text);
}
