////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.logs.text;

import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.logging.logs.text.formatters.ColumnarFormatter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLoggingLogs;

@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
@UmlRelation(label = "formats entries with", referent = LogEntryFormatter.class)
public abstract class BaseTextLog extends BaseLog
{
    private LogEntryFormatter formatter = ColumnarFormatter.DEFAULT;

    public void formatter(final LogEntryFormatter formatter)
    {
        this.formatter = formatter;
    }

    protected String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        return entry.format(formatter, format);
    }
}
