package com.telenav.kivakit.core.function;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Methods helpful for working with functions.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
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
