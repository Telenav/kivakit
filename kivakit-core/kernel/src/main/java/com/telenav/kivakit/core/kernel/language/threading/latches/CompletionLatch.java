////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.latches;

import com.telenav.kivakit.core.kernel.language.threading.status.WakeState;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A simple wrapper around {@link CountDownLatch} that makes code easier to understand. A completion latch can be
 * constructed for one thread with the default constructor or for any number of threads with {@link
 * #CompletionLatch(Count)}. The resulting latch can be waited on by {@link #waitForCompletion()} and {@link
 * #waitForCompletion(Duration)} and completion of a thread can be signaled by calling {@link #completed()}, indicating
 * that the awaited operation has completed. The await() methods return the cause for waking, either {@link
 * WakeState#INTERRUPTED}, {@link WakeState#TIMED_OUT} OR {@link WakeState#COMPLETED}. The method {@link
 * #hasCompleted()} returns true if the operation has completed.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
public class CompletionLatch
{
    private CountDownLatch countdown;

    private final Count threads;

    public CompletionLatch()
    {
        this(Count._1);
    }

    public CompletionLatch(final Count threads)
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
    public WakeState waitForCompletion(final Duration duration)
    {
        try
        {
            return countdown.await(duration.asMilliseconds(), TimeUnit.MILLISECONDS) ? WakeState.COMPLETED : WakeState.TIMED_OUT;
        }
        catch (final InterruptedException ignored)
        {
            return WakeState.INTERRUPTED;
        }
    }

    public WakeState waitForCompletion()
    {
        return waitForCompletion(Duration.MAXIMUM);
    }
}
