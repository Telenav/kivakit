////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.filters;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.messaging.messages.Triaged;

public class SeverityLessThan<T extends Triaged> implements Filter<T>
{
    private final Severity value;

    public SeverityLessThan(final Severity value)
    {
        this.value = value;
    }

    @Override
    public boolean accepts(final T value)
    {
        return value.severity().isLessThan(this.value);
    }

    @Override
    public String toString()
    {
        return "severityLessThan(" + value + ")";
    }
}
