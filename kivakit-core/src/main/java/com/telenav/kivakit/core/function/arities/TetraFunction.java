package com.telenav.kivakit.core.function.arities;

import java.util.function.BiFunction;
import java.util.function.Function;

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
