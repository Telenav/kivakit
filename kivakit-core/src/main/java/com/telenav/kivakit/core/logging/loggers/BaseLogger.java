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

import com.telenav.kivakit.core.collections.map.ConcurrentObjectMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.LoggerCodeContext;
import com.telenav.kivakit.core.logging.filters.LogEntriesWithSeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.messages.OperationMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.lexakai.DiagramLogging;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UmlClassDiagram(diagram = DiagramLogging.class)
public abstract class BaseLogger implements Logger
{
    private static final Map<String, Time> lastLogTime = new ConcurrentObjectMap<>();

    /** The severity level to log */
    private static Severity level;

    private final LoggerCodeContext codeContext;

    private final List<Filter<LogEntry>> filters = new ArrayList<>();

    private final Time start = Time.now();

    protected BaseLogger()
    {
        this(new LoggerCodeContext());
    }

    protected BaseLogger(LoggerCodeContext codeContext)
    {
        this.codeContext = codeContext;
        addFilter(new LogEntriesWithSeverityGreaterThanOrEqualTo(level()));
    }

    @Override
    public void addFilter(Filter<LogEntry> filter)
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
    public void flush(LengthOfTime maximumWaitTime)
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

    @Override
    @UmlExcludeMember
    public Time startTime()
    {
        return start;
    }

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

    @UmlExcludeMember
    protected LogEntry logEntry(LoggerCodeContext context, Thread thread, Message message)
    {
        return new LogEntry(this, context, thread, message);
    }

    @UmlExcludeMember
    protected abstract ObjectSet<Log> logs();

    private static Severity level()
    {
        if (level == null)
        {
            level = Severity.NONE;
            var levelName = System.getProperty("KIVAKIT_LOG_LEVEL");
            if (levelName != null)
            {
                Message message = OperationMessage.parse(Listener.console(), levelName);
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
