package com.telenav.kivakit.core.function;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
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
     * Applies the given function to the input value if it is non-null, returns null otherwise. In some cases, a monad
     * such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @return The mapped value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, Output> Output applyTo(Input value, Function<Input, Output> function)
    {
        return value != null ? function.apply(value) : null;
    }

    /**
     * Applies the given functions to the input value if each function returns a non-null value, returns null otherwise.
     * In some cases, a monad such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @param function2 The second function to apply
     * @return The output value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, T1, Output> Output applyTo(Input value,
                                                     Function<Input, T1> function,
                                                     Function<T1, Output> function2)
    {
        var output = applyTo(value, function);
        if (output != null)
        {
            return function2.apply(output);
        }
        return null;
    }

    /**
     * Applies the given functions to the input value if each function returns a non-null value, returns null otherwise.
     * In some cases, a monad such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @param function2 The second function to apply
     * @param function3 The third function to apply
     * @return The output value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, T1, T2, Output> Output applyTo(Input value,
                                                         Function<Input, T1> function,
                                                         Function<T1, T2> function2,
                                                         Function<T2, Output> function3)
    {
        var output = applyTo(value, function, function2);
        if (output != null)
        {
            return function3.apply(output);
        }
        return null;
    }

    /**
     * Applies the given functions to the input value if each function returns a non-null value, returns null otherwise.
     * In some cases, a monad such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @param function2 The second function to apply
     * @param function3 The third function to apply
     * @param function4 The fourth function to apply
     * @return The output value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, T1, T2, T3, Output> Output applyTo(Input value,
                                                             Function<Input, T1> function,
                                                             Function<T1, T2> function2,
                                                             Function<T2, T3> function3,
                                                             Function<T3, Output> function4)
    {
        var output = applyTo(value, function, function2, function3);
        if (output != null)
        {
            return function4.apply(output);
        }
        return null;
    }

    /**
     * Applies the given functions to the input value if each function returns a non-null value, returns null otherwise.
     * In some cases, a monad such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @param function2 The second function to apply
     * @param function3 The third function to apply
     * @param function4 The fourth function to apply
     * @param function5 The fifth function to apply
     * @return The output value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, T1, T2, T3, T4, Output> Output applyTo(Input value,
                                                                 Function<Input, T1> function,
                                                                 Function<T1, T2> function2,
                                                                 Function<T2, T3> function3,
                                                                 Function<T3, T4> function4,
                                                                 Function<T4, Output> function5)
    {
        var output = applyTo(value, function, function2, function3, function4);
        if (output != null)
        {
            return function5.apply(output);
        }
        return null;
    }

    /**
     * Applies the given functions to the input value if each function returns a non-null value, returns null otherwise.
     * In some cases, a monad such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @param function2 The second function to apply
     * @param function3 The third function to apply
     * @param function4 The fourth function to apply
     * @param function5 The fifth function to apply
     * @param function6 The sixth function to apply
     * @return The output value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, T1, T2, T3, T4, T5, Output> Output applyTo(Input value,
                                                                     Function<Input, T1> function,
                                                                     Function<T1, T2> function2,
                                                                     Function<T2, T3> function3,
                                                                     Function<T3, T4> function4,
                                                                     Function<T4, T5> function5,
                                                                     Function<T5, Output> function6)
    {
        var output = applyTo(value, function, function2, function3, function4, function5);
        if (output != null)
        {
            return function6.apply(output);
        }
        return null;
    }

    /**
     * Applies the given functions to the input value if each function returns a non-null value, returns null otherwise.
     * In some cases, a monad such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @param function2 The second function to apply
     * @param function3 The third function to apply
     * @param function4 The fourth function to apply
     * @param function5 The fifth function to apply
     * @param function6 The sixth function to apply
     * @param function7 The seventh function to apply
     * @return The output value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, T1, T2, T3, T4, T5, T6, Output> Output applyTo(Input value,
                                                                         Function<Input, T1> function,
                                                                         Function<T1, T2> function2,
                                                                         Function<T2, T3> function3,
                                                                         Function<T3, T4> function4,
                                                                         Function<T4, T5> function5,
                                                                         Function<T5, T6> function6,
                                                                         Function<T6, Output> function7)
    {
        var output = applyTo(value, function, function2, function3, function4, function5, function6);
        if (output != null)
        {
            return function7.apply(output);
        }
        return null;
    }

    /**
     * Applies the given functions to the input value if each function returns a non-null value, returns null otherwise.
     * In some cases, a monad such as {@link Maybe} or {@link Result} may be a better approach.
     *
     * @param value The value to apply the function to
     * @param function The function to apply to the value
     * @param function2 The second function to apply
     * @param function3 The third function to apply
     * @param function4 The fourth function to apply
     * @param function5 The fifth function to apply
     * @param function6 The sixth function to apply
     * @param function7 The seventh function to apply
     * @param function8 The eighth function to apply
     * @return The output value, or null if the value was null.
     * @see Maybe
     * @see Result
     */
    public static <Input, T1, T2, T3, T4, T5, T6, T7, Output> Output applyTo(Input value,
                                                                             Function<Input, T1> function,
                                                                             Function<T1, T2> function2,
                                                                             Function<T2, T3> function3,
                                                                             Function<T3, T4> function4,
                                                                             Function<T4, T5> function5,
                                                                             Function<T5, T6> function6,
                                                                             Function<T6, T7> function7,
                                                                             Function<T7, Output> function8)
    {
        var output = applyTo(value, function, function2, function3, function4, function5, function6, function7);
        if (output != null)
        {
            return function8.apply(output);
        }
        return null;
    }

    /**
     * Convenient functional interface for any two-parameter callback
     */
    public static void doNothing(Object ignored, Object ignored2)
    {
    }

    /**
     * Convenient functional interface for any three-parameter callback
     */
    public static void doNothing(Object ignored, Object ignored2, Object ignored3)
    {
    }

    /**
     * Convenient functional interface for any one-parameter callback
     */
    public static void doNothing(Object ignored)
    {
    }

    /**
     * Runs the given list of {@link Function}s, until one returns a non-null value.
     *
     * @param from The value to transform
     * @param functions The functions to apply
     * @return The value returned by the first function that returns a non-null value.
     */
    @SuppressWarnings({ "unused" })
    @SafeVarargs
    public static <From, To> To firstSuccessfulFunction(From from, Function<From, To>... functions)
    {
        for (var function : functions)
        {
            var result = function.apply(from);
            if (result != null)
            {
                return result;
            }
        }
        return null;
    }
}
