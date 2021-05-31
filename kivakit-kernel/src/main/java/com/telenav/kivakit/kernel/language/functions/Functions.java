package com.telenav.kivakit.kernel.language.functions;

import java.util.function.Function;

public class Functions
{
    public static <Input, Output> Output apply(final Input value, final Function<Input, Output> function)
    {
        if (value != null)
        {
            return function.apply(value);
        }
        return null;
    }
}
