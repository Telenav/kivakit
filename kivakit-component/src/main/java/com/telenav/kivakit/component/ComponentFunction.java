package com.telenav.kivakit.component;

import java.util.function.Function;

public interface ComponentFunction<From, To> extends ComponentMixin, Function<From, To>
{
}
