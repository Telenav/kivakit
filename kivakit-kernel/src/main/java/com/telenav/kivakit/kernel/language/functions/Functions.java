package com.telenav.kivakit.kernel.language.functions;

import java.util.function.Function;

public class Functions
{
    public static <Input, Output> Output apply(Input value, Function<Input, Output> function)
    {
        if (value != null)
        {
            return function.apply(value);
        }
        return null;
    }
}
