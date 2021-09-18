package com.telenav.kivakit.component;

import java.util.function.Supplier;

public interface ComponentSupplier<Value> extends ComponentMixin, Supplier<Value>
{
}
