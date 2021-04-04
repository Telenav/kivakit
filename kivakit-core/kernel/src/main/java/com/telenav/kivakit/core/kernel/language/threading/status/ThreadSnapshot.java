package com.telenav.kivakit.core.kernel.language.threading.status;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class ThreadSnapshot
{
    @UmlAggregation
    private List<ThreadStatus> threads;

    private Time time;

    public ThreadSnapshot()
    {
    }

    public Duration cpuTime()
    {
        var milliseconds = 0L;
        for (final var thread : threads)
        {
            milliseconds += thread.cpuTime().asMilliseconds();
        }
        return Duration.milliseconds(milliseconds);
    }

    public List<ThreadStatus> threads()
    {
        return threads;
    }

    public Time time()
    {
        return time;
    }

    public ThreadSnapshot update()
    {
        final var threads = new ArrayList<ThreadStatus>();
        final ThreadMXBean management = ManagementFactory.getThreadMXBean();
        for (final Long identifier : management.getAllThreadIds())
        {
            if (identifier != null)
            {
                final var information = management.getThreadInfo(identifier);
                if (information != null)
                {
                    final var status = new ThreadStatus();
                    status.cpuTime = Duration.milliseconds(management.getThreadCpuTime(identifier) / 1_000_000L);
                    status.isDaemon = information.isDaemon();
                    status.name = information.getThreadName();
                    status.state = information.getThreadState();
                    threads.add(status);
                }
            }
        }
        time = Time.now();
        this.threads = threads;
        return this;
    }
}
