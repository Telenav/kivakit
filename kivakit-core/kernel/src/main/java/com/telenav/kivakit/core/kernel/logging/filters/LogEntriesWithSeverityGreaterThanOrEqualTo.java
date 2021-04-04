////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.filters;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;

public class LogEntriesWithSeverityGreaterThanOrEqualTo implements Filter<LogEntry>
{
    private final Severity severity;

    public LogEntriesWithSeverityGreaterThanOrEqualTo(final Severity severity)
    {
        assert severity != null;
        this.severity = severity;
    }

    @Override
    public boolean accepts(final LogEntry entry)
    {
        return entry.message().severity().isGreaterThanOrEqualTo(severity);
    }

    @Override
    public String toString()
    {
        return "severityGreaterThanOrEqualTo(" + severity + ")";
    }
}
