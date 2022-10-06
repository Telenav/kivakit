package com.telenav.kivakit.core.function;

import com.telenav.kivakit.annotations.code.CodeQuality;

import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Methods helpful for working with functions.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class Functions
{
    /**
     * Applies the given function to the input value if it is non-null, returns null otherwise. In many cases, a monad
     * will be a better approach. See {@link Maybe} and {@link Result}.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @return The mapped value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, Output> Output apply(Input value, Function<Input, Output> function)
    {
        return value != null ? function.apply(value) : null;
    }
}
