////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.filters;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.language.types.Classes;

public class SubClassesOf<T> implements Filter<T>
{
    private final Class<T> type;

    public SubClassesOf(final Class<T> type)
    {
        this.type = type;
    }

    @Override
    public boolean accepts(final Object value)
    {
        if (value == null)
        {
            return false;
        }
        return value.getClass().isAssignableFrom(type);
    }

    @Override
    public String toString()
    {
        return "subClassOf(" + Classes.simpleName(type) + ")";
    }
}
