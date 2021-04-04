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

import java.util.ArrayList;
import java.util.List;

public class LogEntriesOfType implements Filter<LogEntry>
{
    private final Class<? extends Message>[] types;

    private final List<Class<?>> includedClasses = new ArrayList<>();

    @SafeVarargs
    public LogEntriesOfType(final Class<? extends Message>... types)
    {
        this.types = types;
    }

    @Override
    public boolean accepts(final LogEntry value)
    {
        if (!includedClasses.isEmpty())
        {
            if (!includedClasses.contains(value.context().type()))
            {
                return false;
            }
        }
        for (final var type : types)
        {
            if (type.equals(value.message().getClass()))
            {
                return true;
            }
        }
        return false;
    }

    public void fromClass(final Class<?> within)
    {
        includedClasses.add(within);
    }

    @Override
    public String toString()
    {
        final var names = new StringList();
        for (final var type : types)
        {
            names.add(Classes.simpleName(type));
        }
        return "logEntriesOfType(" + names + ")";
    }
}
