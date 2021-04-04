////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.filters;

import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.MessageFilter;

public class SeverityGreaterThanOrEqualTo implements MessageFilter
{
    private final Severity value;

    public SeverityGreaterThanOrEqualTo(final Severity value)
    {
        this.value = value;
    }

    @Override
    public boolean accepts(final Message value)
    {
        return value.severity().isGreaterThanOrEqualTo(this.value);
    }

    @Override
    public String toString()
    {
        return "severityGreaterThan(" + value + ")";
    }
}
