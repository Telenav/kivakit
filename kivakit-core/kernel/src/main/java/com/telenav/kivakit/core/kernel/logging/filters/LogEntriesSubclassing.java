////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.filters;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.messaging.Message;

public class LogEntriesSubclassing implements Filter<LogEntry>
{
    private final Class<? extends Message>[] types;

    @SafeVarargs
    public LogEntriesSubclassing(final Class<? extends Message>... types)
    {
        this.types = types;
    }

    @Override
    public boolean accepts(final LogEntry value)
    {
        for (final var type : types)
        {
            if (type.isAssignableFrom(value.message().getClass()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        final var names = new StringList();
        for (final var type : types)
        {
            names.add(Classes.simpleName(type));
        }
        return "logEntriesSubclassing(" + names + ")";
    }
}
