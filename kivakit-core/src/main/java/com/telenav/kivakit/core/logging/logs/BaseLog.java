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

import com.telenav.kivakit.core.collections.map.CountMap;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.filters.LogEntriesWithSeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.os.ConsoleWriter;
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

import static com.telenav.kivakit.core.thread.KivaKitThread.State.STOP_REQUESTED;
import static com.telenav.kivakit.core.time.Frequency.CONTINUOUSLY;
import static com.telenav.kivakit.core.vm.ShutdownHook.Order.LAST;

/**
 * Base class for log implementations. Handles background queueing of log entries.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
@UmlClassDiagram(diagram = DiagramLogs.class)
public abstract class BaseLog implements
        Startable,
        Stoppable<Duration>,
        Log
{
    private static volatile boolean isAsynchronous;

    private static final List<BaseLog> logs = new ArrayList<>();

    static
    {
        isAsynchronous = Properties.isPropertyFalse("KIVAKIT_LOG_SYNCHRONOUS");
    }

    public static void asynchronous(boolean asynchronous)
    {
        isAsynchronous = asynchronous;
    }

    public static boolean isAsynchronous()
    {
        return isAsynchronous;
    }

    public static List<BaseLog> logs()
    {
        return logs;
    }

    private volatile boolean closed;

    private final List<Filter<LogEntry>> filters = new ArrayList<>();

    private final CountMap<String> messageCounts = new CountMap<>();

    private final ArrayBlockingQueue<LogEntry> queue = new ArrayBlockingQueue<>(queueSize());

    private final AtomicBoolean started = new AtomicBoolean();

    private RepeatingThread thread;

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

    public void addFilter(Filter<LogEntry> filter)
    {
        filters.add(filter);
    }

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

    public void closeOutput()
    {
    }

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

    @Override
    public List<Filter<LogEntry>> filters()
    {
        return filters;
    }

    @Override
    public void flush(Duration maximumWaitTime)
    {
        if (thread != null)
        {
            asynchronous(false);
            thread.interrupt();
            queueEmpty.waitFor(true, maximumWaitTime);
        }
    }

    @Override
    public int hashCode()
    {
        return name().hashCode();
    }

    public boolean isClosed()
    {
        return closed;
    }

    @Override
    public boolean isRunning()
    {
        return thread != null && thread.isRunning();
    }

    @Override
    public void level(Severity minimum)
    {
        if (minimum != null)
        {
            addFilter(new LogEntriesWithSeverityGreaterThanOrEqualTo(minimum));
        }
    }

    @Override
    public final void log(LogEntry entry)
    {
        assert entry.context() != null;
        if (!closed && accept(entry))
        {
            JavaVirtualMachine.local().health().logEntry(entry);

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

    @Override
    public final Duration maximumWaitTime()
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

    @Override
    public String name()
    {
        return Classes.simpleName(getClass());
    }

    @Override
    public final boolean start()
    {
        thread = new RepeatingThread(Listener.emptyListener(), name() + "-Log", CONTINUOUSLY)
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
                addListener(new ConsoleWriter());
            }
        };
        return thread.start();
    }

    @Override
    public void stop(Duration wait)
    {
        close();
        flush(wait);
        if (thread != null)
        {
            thread.stop(wait);
        }
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
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

    protected abstract void onLog(LogEntry entry);

    protected void onLogFailure(LogEntry entry)
    {
        System.out.println("Failed to log: " + entry);
    }

    @SuppressWarnings("SameReturnValue")
    @KivaKitIncludeProperty
    protected int queueSize()
    {
        return 20_000;
    }

    @SuppressWarnings("SameReturnValue")
    @KivaKitIncludeProperty
    protected int retries()
    {
        return 3;
    }

    private void checkForEmptyQueue()
    {
        if (queue.isEmpty())
        {
            queueEmpty.signal(true);
        }
    }

    private boolean dispatch(LogEntry entry)
    {
        if (entry.severity().isGreaterThan(Severity.NONE))
        {
            synchronized (messageCounts)
            {
                messageCounts.increment(Plural.pluralize(entry.messageType()));
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
