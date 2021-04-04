////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.filters.operators;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;

public class All<T> implements Filter<T>
{
    @Override
    public boolean accepts(final T value)
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "all";
    }
}
