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

package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Captures a snapshot of {@link ThreadStatus} for all running threads via the Java management API.
 *
 * @author jonathanl (shibo)
 * @see ThreadStatus
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramThread.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class ThreadSnapshot
{
    /** The status of each thread */
    @UmlAggregation
    private List<ThreadStatus> threads = new ArrayList<>();

    /** The time at which this snapshot was captured */
    private Time capturedAt;

    public ThreadSnapshot()
    {
    }

    /**
     * @return The time at which this snapshot was captured
     */
    public Time capturedAt()
    {
        return capturedAt;
    }

    /**
     * Returns the CPU time consumed by the current thread
     */
    public Duration cpuTime()
    {
        return cpuTime(Thread.currentThread());
    }

    /**
     * Returns the CPU time consumed by the given thread
     */
    public Duration cpuTime(Thread thread)
    {
        for (var status : threads)
        {
            if (status.identifier == thread.getId())
            {
                return status.cpuTime;
            }
        }
        return null;
    }

    /**
     * @return The status of all threads captured in the snapshot
     */
    public List<ThreadStatus> snapshot()
    {
        return threads;
    }

    /**
     * @return The total CPU time of all threads
     */
    public Duration totalCpuTime()
    {
        var milliseconds = 0L;
        for (var thread : threads)
        {
            milliseconds += thread.cpuTime().asMilliseconds();
        }
        return Duration.milliseconds(milliseconds);
    }

    /**
     * @return Updates this thread snapshot
     */
    public ThreadSnapshot update()
    {
        var threads = new ArrayList<ThreadStatus>();
        ThreadMXBean management = ManagementFactory.getThreadMXBean();
        for (Long identifier : management.getAllThreadIds())
        {
            if (identifier != null)
            {
                var information = management.getThreadInfo(identifier);
                if (information != null)
                {
                    var status = new ThreadStatus();
                    status.cpuTime = Duration.milliseconds(management.getThreadCpuTime(identifier) / 1_000_000L);
                    status.isDaemon = information.isDaemon();
                    status.name = information.getThreadName();
                    status.identifier = information.getThreadId();
                    status.state = information.getThreadState();
                    threads.add(status);
                }
            }
        }
        capturedAt = Time.now();
        this.threads = threads;
        return this;
    }
}
