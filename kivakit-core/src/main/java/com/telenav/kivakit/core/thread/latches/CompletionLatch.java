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

import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.interfaces.time.WakeState;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A simple wrapper around {@link CountDownLatch} that makes code easier to understand. A completion latch can be
 * constructed for one thread with the default constructor or for any number of threads with {@link
 * #CompletionLatch(Count)}. The resulting latch can be waited on by {@link #waitForCompletion()} and {@link
 * #waitForCompletion(Duration)} and completion of a thread can be signaled by calling {@link #completed()}, indicating
 * that the awaited operation has completed. The <i>wait*()</i> methods return the cause for waking, either {@link
 * WakeState#INTERRUPTED}, {@link WakeState#TIMED_OUT} OR {@link WakeState#COMPLETED}. The method {@link
 * #hasCompleted()} returns true if the operation has completed.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
public class CompletionLatch
{
    private CountDownLatch countdown;

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

    public void completed()
    {
        countdown.countDown();
    }

    public boolean hasCompleted()
    {
        return countdown.getCount() == 0;
    }

    public void reset()
    {
        countdown = new CountDownLatch(threads.asInt());
    }

    @UmlRelation(label = "waits until")
    public WakeState waitForCompletion(Duration duration)
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

    public WakeState waitForCompletion()
    {
        return waitForCompletion(Duration.MAXIMUM);
    }
}
