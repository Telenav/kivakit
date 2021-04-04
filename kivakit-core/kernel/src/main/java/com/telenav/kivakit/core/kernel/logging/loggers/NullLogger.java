////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.loggers;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A logger that does nothing.
 * <p>
 * The following code can be used to efficiently shut off all logging:
 * </p>
 * <pre>
 * LogFactory.factory(NullLogger::new)
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
public class NullLogger implements Logger
{
    @Override
    public void addFilter(final Filter<LogEntry> filter)
    {
    }

    @Override
    public LoggerCodeContext codeContext()
    {
        return new LoggerCodeContext();
    }

    @Override
    public List<Filter<LogEntry>> filters()
    {
        return List.of();
    }

    @Override
    public void flush(final Duration maximumWaitTime)
    {
    }

    @Override
    public void log(final Message message)
    {
    }

    @Override
    public void log(final LoggerCodeContext context, final Thread thread, final Message message)
    {
    }

    @Override
    public Time startTime()
    {
        return Time.now();
    }
}
