////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.filters.operators;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;

public class Or<T> implements Filter<T>
{
    private final Filter<T> a;

    private final Filter<T> b;

    public Or(final Filter<T> a, final Filter<T> b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean accepts(final T value)
    {
        return a.accepts(value) || b.accepts(value);
    }

    @Override
    public String toString()
    {
        return a + " or " + b;
    }
}
