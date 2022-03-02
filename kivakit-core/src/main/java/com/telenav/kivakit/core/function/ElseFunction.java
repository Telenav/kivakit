package com.telenav.kivakit.core.function;

import java.util.function.Function;

/**
 * Adds {@link #elseApply(Object)} as an alias of {@link #apply(Object)} to improve readability of {@link Maybe}.
 *
 * @author jonathanl (shibo)
 * @see <a href="https://github.com/viniciusluisr/improved-optional/blob/master/ElseFunction.java">improved-optional</a>
 */
@FunctionalInterface
public interface ElseFunction<Input, Output> extends Function<Input, Output>
{
    default Output elseApply(Input input)
    {
        return apply(input);
    }
}
