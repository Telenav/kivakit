////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.logs.text.formatters;

import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.logs.text.LogEntryFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;

public class TimeAndMessageFormatter implements LogEntryFormatter
{
    @Override
    public String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        return entry.message().created() + " " + entry.formattedMessage(format);
    }
}
