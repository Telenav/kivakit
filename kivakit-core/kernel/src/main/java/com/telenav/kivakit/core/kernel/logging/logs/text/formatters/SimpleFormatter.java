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
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLoggingLogs;

@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
public class SimpleFormatter implements LogEntryFormatter
{
    @Override
    public String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        return entry.message().created().utc() + " " + entry.context() + " "
                + Classes.simpleName(entry.message().getClass()) + " " + entry.threadName() + ": "
                + entry.formattedMessage(format);
    }
}
