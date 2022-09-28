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

package com.telenav.kivakit.core.thread.latches;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.time.WakeState;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A simple wrapper around {@link CountDownLatch} that makes code easier to understand. A completion latch can be
 * constructed for one thread with the default constructor or for any number of threads with
 * {@link #CompletionLatch(Count)}. The resulting latch can be waited on by {@link #waitForAllThreadsToComplete()} and
 * {@link #waitForAllThreadsToComplete(Duration)} and completion of a thread can be signaled by calling
 * {@link #threadCompleted()}, indicating that the awaited operation has completed. The <i>wait*()</i> methods return
 * the cause for waking, either {@link WakeState#INTERRUPTED}, {@link WakeState#TIMED_OUT} OR
 * {@link WakeState#COMPLETED}. The method {@link #allThreadsHaveCompleted()} returns true if the operation has
 * completed.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramThread.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class CompletionLatch
{
    /** The underlying countdown latch */
    private CountDownLatch countdown;

    /** The number of threads that need to complete */
    private final Count threads;

    public CompletionLatch()
    {
        this(Count._1);
    }

    public CompletionLatch(Count threads)
    {
        this.threads = threads;
        reset();
    }

    /**
     * Returns true when all threads have completed
     */
    public boolean allThreadsHaveCompleted()
    {
        return countdown.getCount() == 0;
    }

    /**
     * Resets this latch for another use
     */
    public void reset()
    {
        countdown = new CountDownLatch(threads.asInt());
    }

    /**
     * Called when a thread completes
     */
    public void threadCompleted()
    {
        countdown.countDown();
    }

    @UmlRelation(label = "waits until")
    public WakeState waitForAllThreadsToComplete(Duration duration)
    {
        try
        {
            return countdown.await(duration.milliseconds(), TimeUnit.MILLISECONDS) ? WakeState.COMPLETED : WakeState.TIMED_OUT;
        }
        catch (InterruptedException ignored)
        {
            return WakeState.INTERRUPTED;
        }
    }

    public WakeState waitForAllThreadsToComplete()
    {
        return waitForAllThreadsToComplete(Duration.MAXIMUM);
    }
}
