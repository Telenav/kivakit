////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.loggers;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.language.collections.map.BaseConcurrentMap;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.core.kernel.logging.filters.LogEntriesWithSeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.OperationMessage;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.project.CoreKernelLimits;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UmlClassDiagram(diagram = DiagramLogging.class)
public abstract class BaseLogger implements Logger
{
    private static final Map<String, Time> lastLogTime = new BaseConcurrentMap<>(CoreKernelLimits.UNIQUE_LOG_ENTRIES);

    /** The severity level to log */
    private static Severity level;

    private final LoggerCodeContext codeContext;

    private final List<Filter<LogEntry>> filters = new ArrayList<>();

    private final Time start = Time.now();

    protected BaseLogger()
    {
        this(new LoggerCodeContext());
    }

    protected BaseLogger(final LoggerCodeContext codeContext)
    {
        this.codeContext = codeContext;
        addFilter(new LogEntriesWithSeverityGreaterThanOrEqualTo(level()));
    }

    @Override
    public void addFilter(final Filter<LogEntry> filter)
    {
        filters.add(filter);
    }

    @Override
    @UmlExcludeMember
    public LoggerCodeContext codeContext()
    {
        return codeContext;
    }

    @Override
    public List<Filter<LogEntry>> filters()
    {
        return filters;
    }

    @Override
    @UmlExcludeMember
    public void flush(final Duration maximumWaitTime)
    {
        final var logs = logs();
        for (final var log : logs)
        {
            log.flush(maximumWaitTime.divide(logs.size()));
        }
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Logs the given message using the given context and thread
     */
    @Override
    @UmlExcludeMember
    public void log(final LoggerCodeContext context, final Thread thread, final Message message)
    {
        if (isTimeToLog(message))
        {
            final var entry = logEntry(context, thread, message);
            if (accept(entry))
            {
                for (final var log : logs())
                {
                    log.log(entry);
                }
            }
        }
    }

    /**
     * Logs the given message using this loggers {@link LoggerCodeContext} and thread.
     */
    @Override
    @UmlExcludeMember
    public void log(final Message message)
    {
        log(codeContext, Thread.currentThread(), message);
    }

    @Override
    @UmlExcludeMember
    public Time startTime()
    {
        return start;
    }

    protected final boolean accept(final LogEntry entry)
    {
        for (final var filter : filters)
        {
            if (filter != null && !filter.accepts(entry))
            {
                return false;
            }
        }
        return true;
    }

    @UmlExcludeMember
    protected boolean isTimeToLog(final Message message)
    {
        // If the message has a maximum frequency,
        final var frequency = message.maximumFrequency();
        if (frequency != null)
        {
            // find out when this message was last logged
            final var time = lastLogTime.get(message.text());

            // and if it has never been logged or it was logged long enough ago,
            if (time == null || time.isOlderThan(frequency.duration()))
            {
                // reset the last log time and let it be logged
                lastLogTime.put(message.text(), Time.now());
                return true;
            }
            else
            {
                // otherwise, it hasn't been long enough so don't log
                return false;
            }
        }
        return true;
    }

    @UmlExcludeMember
    protected LogEntry logEntry(final LoggerCodeContext context, final Thread thread, final Message message)
    {
        return new LogEntry(this, context, thread, message);
    }

    @UmlExcludeMember
    protected abstract Set<Log> logs();

    private static Severity level()
    {
        if (level == null)
        {
            level = Severity.NONE;
            final var levelName = System.getProperty("KIVAKIT_LOG_LEVEL");
            if (levelName != null)
            {
                final Message message = OperationMessage.of(levelName);
                if (message != null)
                {
                    level = message.severity();
                }
                else
                {
                    return Ensure.fail("Unrecognized KIVAKIT_LOG_LEVEL '" + levelName + "'");
                }
            }
        }
        return level;
    }
}
