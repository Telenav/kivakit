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

package com.telenav.kivakit.core.logging.logs;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.CountMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.filters.LogEntriesWithSeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.string.Plural;
import com.telenav.kivakit.core.thread.RepeatingThread;
import com.telenav.kivakit.core.thread.StateWatcher;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.vm.Properties;
import com.telenav.kivakit.core.vm.ShutdownHook;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.messages.Severity.NONE;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.STOP_REQUESTED;
import static com.telenav.kivakit.core.time.Frequency.CONTINUOUSLY;
import static com.telenav.kivakit.core.vm.ShutdownHook.Order.LAST;

/**
 * Base class for log implementations. Handles background queueing of log entries. By default, logging is asynchronous.
 * To make logging synchronous (for example, when debugging), set the property KIVAKIT_LOG_SYNCHRONOUS to false.
 *
 * <p><b>Configuration</b></p>
 *
 * <ul>
 *     <li>{@link #logs()}</li>
 *     <li>{@link #level(Severity)}</li>
 *     <li>{@link #maximumFlushTime()}</li>
 *     <li>{@link #maximumStopTime()}</li>
 * </ul>
 *
 * <p><b>Logging</b></p>
 *
 * <ul>
 *     <li>{@link #name()}</li>
 *     <li>{@link #log(LogEntry)}</li>
 *     <li>{@link #clear()}</li>
 *     <li>{@link #flush(Duration)}</li>
 *     <li>{@link #closeOutput()}</li>
 *     <li>{@link #close()}</li>
 *     <li>{@link #isClosed()}</li>
 *     <li>{@link #messageCounts()}</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #addFilter(Filter)}</li>
 *     <li>{@link #filters()}</li>
 * </ul>
 *
 * <p><b>Asynchronous Logging</b></p>
 *
 * <ul>
 *     <li>{@link #isAsynchronous}</li>
 *     <li>{@link #asynchronous(boolean)}</li>
 *     <li>{@link #start()}</li>
 *     <li>{@link #stop(Duration)}</li>
 *     <li>{@link #isRunning()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
@UmlClassDiagram(diagram = DiagramLogs.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseLog implements
        Startable,
        Stoppable<Duration>,
        Log
{
    /** True if logging is asynchronous (applies to all logs) */
    private static volatile boolean isAsynchronous;

    /** A collection of all logs in use */
    private static final List<BaseLog> logs = new ArrayList<>();

    static
    {
        // Determine if we are asynchronous or not
        isAsynchronous = Properties.isSystemPropertyOrEnvironmentVariableFalse("KIVAKIT_LOG_SYNCHRONOUS");
    }

    /**
     * Set asynchronicity
     */
    public static void asynchronous(boolean asynchronous)
    {
        isAsynchronous = asynchronous;
    }

    /**
     * Returns true if logging is asynchronous
     */
    public static boolean isAsynchronous()
    {
        return isAsynchronous;
    }

    /**
     * Returns the list of logs in use
     */
    public static List<BaseLog> logs()
    {
        return logs;
    }

    /** True if this log is closed and will accept no further entries */
    private volatile boolean closed;

    /** List of log entry filters */
    private final ObjectList<Filter<LogEntry>> filters = new ObjectList<>();

    /** The number of each type of message that has been logged */
    private final CountMap<String> messageCounts = new CountMap<>();

    /** Queue of log entries to write asynchronously */
    private final ArrayBlockingQueue<LogEntry> queue = new ArrayBlockingQueue<>(queueSize());

    /** True if this log has started */
    private final AtomicBoolean started = new AtomicBoolean();

    /** Background thread to write log entries */
    private RepeatingThread writerThread;

    /** State that indicates the queue is empty */
    final StateWatcher<Boolean> queueEmpty = new StateWatcher<>(true);

    protected BaseLog()
    {
        logs.add(this);

        // If we are asynchronous,
        if (isAsynchronous())
        {
            // when the VM shuts down
            ShutdownHook.register(getClass().getSimpleName() + ".flush()", LAST, () ->
            {
                // flush asynchronous entries for up to one minute
                flush(Duration.ONE_MINUTE);
            });
        }
    }

    /**
     * Adds the given filter to this log
     *
     * @param filter The filter
     */
    public void addFilter(Filter<LogEntry> filter)
    {
        filters.add(filter);
    }

    /**
     * Clears this log (if the log is transient, such as a log stored in memory
     */
    public void clear()
    {
    }

    /**
     * Closes the queue to new entries
     */
    @Override
    public void close()
    {
        closed = true;
    }

    /**
     * Closes log output, such as when logs are rolled over
     */
    public void closeOutput()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof BaseLog)
        {
            var that = (BaseLog) object;
            return name().equals(that.name());
        }
        return false;
    }

    /**
     * Retrieves the log entry filters for this log
     */
    @Override
    public List<Filter<LogEntry>> filters()
    {
        return filters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush(Duration maximumWaitTime)
    {
        if (writerThread != null)
        {
            asynchronous(false);
            writerThread.interrupt();
            queueEmpty.waitFor(true, maximumWaitTime);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return name().hashCode();
    }

    /**
     * Returns true if this log is closed to further entries
     */
    public boolean isClosed()
    {
        return closed;
    }

    /**
     * Returns true if the background writer thread is running
     */
    @Override
    public boolean isRunning()
    {
        return writerThread != null && writerThread.isRunning();
    }

    /**
     * Sets the minimum severity for entries to log
     *
     * @param minimum The minimum severity of entries to log
     */
    @Override
    public void level(Severity minimum)
    {
        if (minimum != null)
        {
            addFilter(new LogEntriesWithSeverityGreaterThanOrEqualTo(minimum));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void log(LogEntry entry)
    {
        ensureNotNull(entry.context());

        if (!closed && accept(entry))
        {
            JavaVirtualMachine.javaVirtualMachine().health().logEntry(entry);

            if (isAsynchronous())
            {
                if (!started.getAndSet(true))
                {
                    start();
                }
                try
                {
                    queue.put(entry);
                }
                catch (InterruptedException ignored)
                {
                }
            }
            else
            {
                if (!dispatch(entry))
                {
                    onLogFailure(entry);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Duration maximumStopTime()
    {
        return Duration.MAXIMUM;
    }

    /**
     * @return The number of times each type of message has been logged so far
     */
    public CountMap<String> messageCounts()
    {
        synchronized (messageCounts)
        {
            return new CountMap<>(messageCounts);
        }
    }

    /**
     * Returns the name of this log
     */
    @Override
    public String name()
    {
        return Classes.simpleName(getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean start()
    {
        writerThread = new RepeatingThread(Listener.nullListener(), name() + "-Log", CONTINUOUSLY)
        {
            @Override
            protected void onRun()
            {
                if (!is(STOP_REQUESTED))
                {
                    try
                    {
                        var entry = queue.take();
                        if (!dispatch(entry))
                        {
                            retry(entry);
                        }
                        checkForEmptyQueue();
                    }
                    catch (InterruptedException ignored)
                    {
                        checkForEmptyQueue();
                    }
                }
            }

            private void retry(LogEntry entry)
            {
                // Try a few times to write the failed log entry
                var success = false;
                for (var i = 0; i < retries(); i++)
                {
                    if (dispatch(entry))
                    {
                        success = true;
                        break;
                    }
                }

                // If we are unable to write in several tries,
                if (!success)
                {
                    // log the entry as a failure
                    onLogFailure(entry);

                    // and then drain the rest of the queue as failures to
                    // prevent the queue from blocking
                    List<LogEntry> failures = new ArrayList<>();
                    queue.drainTo(failures);
                    checkForEmptyQueue();
                    for (var failure : failures)
                    {
                        onLogFailure(failure);
                    }
                }
            }

            {
                addListener(new Console());
            }
        };
        return writerThread.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(Duration wait)
    {
        close();
        flush(wait);
        if (writerThread != null)
        {
            writerThread.stop(wait);
        }
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Accepts or rejects the given log entry
     *
     * @param entry The entry to inspect
     * @return True if the entry is accepted, false otherwise
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
     * {@inheritDoc}
     */
    protected abstract void onLog(LogEntry entry);

    /**
     * Called if an entry is dropped
     */
    protected void onLogFailure(LogEntry entry)
    {
        System.out.println("Failed to log: " + entry);
    }

    /**
     * The maximum queue size for asynchronous logging
     */
    @SuppressWarnings("SameReturnValue")
    @KivaKitIncludeProperty
    protected int queueSize()
    {
        return 20_000;
    }

    /**
     * Returns the number of retries that should be attempted before giving up on logging an entry
     */
    @SuppressWarnings("SameReturnValue")
    @KivaKitIncludeProperty
    protected int retries()
    {
        return 3;
    }

    /**
     * Returns true if the queue is empty
     */
    private void checkForEmptyQueue()
    {
        if (queue.isEmpty())
        {
            queueEmpty.signal(true);
        }
    }

    /**
     * Increments the message count for the type of message, and dispatches the log entry by calling
     * {@link #onLog(LogEntry)}
     *
     * @return True if the entry was logged successfully
     */
    private boolean dispatch(LogEntry entry)
    {
        if (entry.severity().isGreaterThan(NONE))
        {
            synchronized (messageCounts)
            {
                messageCounts.increment(Plural.pluralizeEnglish(entry.messageType()));
            }
        }
        var success = true;
        try
        {
            onLog(entry);
        }
        catch (Exception e)
        {
            System.err.println(new Problem(e, "Failed to write log entry").asString());
            success = false;
        }
        return success;
    }
}
