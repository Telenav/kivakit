////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.threading.status;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Captures a snapshot of {@link ThreadStatus} for all running threads via the Java management API.
 *
 * @author jonathanl (shibo)
 * @see ThreadStatus
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
@LexakaiJavadoc(complete = true)
public class ThreadSnapshot
{
    /** The status of each thread */
    @UmlAggregation
    private List<ThreadStatus> threads;

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
        for (final var thread : threads)
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
        capturedAt = Time.now();
        this.threads = threads;
        return this;
    }
}
