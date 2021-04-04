////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.logs.text;

import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLoggingLogs;

/**
 * Something that formats log entries in a text log
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
public interface LogEntryFormatter
{
    String format(LogEntry entry, MessageFormatter.Format format);
}
