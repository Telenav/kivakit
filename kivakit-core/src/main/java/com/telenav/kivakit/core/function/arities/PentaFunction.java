package com.telenav.kivakit.core.function.arities;

import com.telenav.kivakit.annotations.code.CodeQuality;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Represents a function that accepts two arguments and produces a result. This is the five-arity specialization of
 * {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object, Object, Object)}.
 *
 * @param <Argument1> The type of the first argument to the function
 * @param <Argument2> The type of the second argument to the function
 * @param <Argument3> The type of the third argument to the function
 * @param <Argument4> The type of the fourth argument to the function
 * @param <Argument5> The type of the fifth argument to the function
 * @param <Result> the type of the result of the function
 * @see BiFunction
 */
@SuppressWarnings("SpellCheckingInspection")
@FunctionalInterface
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface PentaFunction<Argument1, Argument2, Argument3, Argument4, Argument5, Result>
{
    /**
     * Applies this function to the given arguments.
     *
     * @param argument1 The first function argument
     * @param argument2 The second function argument
     * @param argument3 The third function argument
     * @param argument4 The fourth function argument
     * @param argument5 The fifth function argument
     * @return The function result
     */
    Result apply(Argument1 argument1,
                 Argument2 argument2,
                 Argument3 argument3,
                 Argument4 argument4,
                 Argument5 argument5);
}
