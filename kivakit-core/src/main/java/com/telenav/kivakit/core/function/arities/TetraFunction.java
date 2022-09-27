package com.telenav.kivakit.core.function.arities;

import com.telenav.kivakit.annotations.code.ApiQuality;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Represents a function that accepts two arguments and produces a result. This is the four-arity specialization of
 * {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object, Object)}.
 *
 * @param <Argument1> The type of the first argument to the function
 * @param <Argument2> The type of the second argument to the function
 * @param <Argument3> The type of the third argument to the function
 * @param <Argument4> The type of the fourth argument to the function
 * @param <Result> the type of the result of the function
 * @see BiFunction
 */
@FunctionalInterface
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public interface TetraFunction<Argument1, Argument2, Argument3, Argument4, Result>
{
    /**
     * Applies this function to the given arguments.
     *
     * @param argument1 The first function argument
     * @param argument2 The second function argument
     * @param argument3 The third function argument
     * @param argument4 The fourth function argument
     * @return The function result
     */
    Result apply(Argument1 argument1,
                 Argument2 argument2,
                 Argument3 argument3,
                 Argument4 argument4);
}
