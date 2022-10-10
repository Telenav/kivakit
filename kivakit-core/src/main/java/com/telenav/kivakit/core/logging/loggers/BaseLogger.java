////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.logging.loggers;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ConcurrentObjectMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerCodeContext;
import com.telenav.kivakit.core.logging.filters.LogEntriesWithSeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.OperationMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Base implementation of the {@link Logger} interface
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseLogger implements Logger
{
    /** Map from (un-interpolated) message text to the last time the message was logged */
    private static final Map<String, Time> lastLogTime = new ConcurrentObjectMap<>();

    /** The severity level to log */
    private static Severity level;

    /** The code context of the logger (what class it is in) */
    private final LoggerCodeContext codeContext;

    /** List of log entry filters */
    private final ObjectList<Filter<LogEntry>> filters = ObjectList.objectList();

    /** The time that this logger was constructed */
    private final Time start = Time.now();

    /**
     * Constructs with implicit code context determined from stack analysis
     */
    protected BaseLogger()
    {
        this(new LoggerCodeContext());
    }

    /**
     * Constructs with the given code context
     */
    protected BaseLogger(LoggerCodeContext codeContext)
    {
        this.codeContext = codeContext;
        addFilter(new LogEntriesWithSeverityGreaterThanOrEqualTo(minimumSeverityLevel()));
    }

    /**
     * Adds a log entry filter to this logger
     */
    @Override
    public void addFilter(Filter<LogEntry> filter)
    {
        filters.add(filter);
    }

    /**
     * Returns the code context that instantiated this logger
     */
    @Override
    @UmlExcludeMember
    public LoggerCodeContext codeContext()
    {
        return codeContext;
    }

    /**
     * Returns the log entry filters installed on this logger
     */
    @Override
    public ObjectList<Filter<LogEntry>> filters()
    {
        return filters;
    }

    /**
     * Flushes each log that this logger has been writing to
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    @UmlExcludeMember
    public void flush(Duration maximumWaitTime)
    {
        var logs = logs();
        for (var log : logs)
        {
            log.flush(maximumWaitTime.dividedBy(logs.size()));
        }
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Logs the given message using the given context and thread
     */
    @Override
    @UmlExcludeMember
    public void log(LoggerCodeContext context, Thread thread, Message message)
    {
        if (isTimeToLog(message))
        {
            var entry = logEntry(context, thread, message);
            if (accept(entry))
            {
                for (var log : logs())
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
    public void log(Message message)
    {
        log(codeContext, Thread.currentThread(), message);
    }

    /**
     * Returns the maximum time to wait for flushing to complete
     */
    @Override
    public Duration maximumFlushTime()
    {
        return Duration.MAXIMUM;
    }

    /**
     * Returns the time that this logger started
     */
    @Override
    @UmlExcludeMember
    public Time startTime()
    {
        return start;
    }

    /**
     * Returns true if this logger accepts the given log entry
     */
    protected final boolean accept(LogEntry entry)
    {
        for (var filter : filters)
        {
            if (filter != null && !filter.accepts(entry))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * If the given message has a maximum frequency, returns true if it has been long enough to log it
     */
    @UmlExcludeMember
    protected boolean isTimeToLog(Message message)
    {
        // If the message has a maximum frequency,
        var frequency = message.maximumFrequency();
        if (frequency != null)
        {
            // find out when this message was last logged
            var time = lastLogTime.get(message.text());

            // and if it has never been logged, or it was logged long enough ago,
            if (time == null || time.isOlderThan(frequency.cycleLength()))
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

    /**
     * <b>Not public API</b>
     * <p>
     * Creates a log entry for the given thread, code context and message.
     */
    @UmlExcludeMember
    protected LogEntry logEntry(LoggerCodeContext context, Thread thread, Message message)
    {
        return new LogEntry(this, context, thread, message);
    }

    /**
     * Returns the logs that this logger should write to
     */
    @UmlExcludeMember
    protected abstract ObjectSet<Log> logs();

    /**
     * Returns the minimum severity level for logging entries
     */
    private static Severity minimumSeverityLevel()
    {
        if (level == null)
        {
            level = Severity.NONE;
            var levelName = System.getProperty("KIVAKIT_LOG_LEVEL");
            if (levelName != null)
            {
                Message message = OperationMessage.parseMessageType(Listener.consoleListener(), levelName);
                if (message != null)
                {
                    level = message.severity();
                }
                else
                {
                    Ensure.fail("Unrecognized KIVAKIT_LOG_LEVEL '" + levelName + "'");
                }
            }
        }

        return level;
    }
}
