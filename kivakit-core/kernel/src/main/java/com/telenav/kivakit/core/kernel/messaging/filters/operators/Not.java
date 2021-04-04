////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.filters.operators;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;

public class Not<T> implements Filter<T>
{
    private final Filter<T> filter;

    public Not(final Filter<T> filter)
    {
        this.filter = filter;
    }

    @Override
    public boolean accepts(final T value)
    {
        return !filter.accepts(value);
    }

    @Override
    public String toString()
    {
        return "!" + filter;
    }
}
