////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.filters.operators;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;

public class None<T> implements Filter<T>
{
    @Override
    public boolean accepts(final T value)
    {
        return false;
    }

    @Override
    public String toString()
    {
        return "none";
    }
}
