////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading;

import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Pausable;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.ConditionLock;
import com.telenav.kivakit.core.kernel.language.threading.locks.legacy.NotifyAllBooleanLock;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A thread that repeatedly executes the {@link #onRun()} method implementation at a given {@link Frequency}. The thread
 * can be paused, resumed, stopped and started.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public abstract class RepeatingThread extends KivaKitThread implements Pausable, Stoppable
{
    private Frequency frequency;

    private final ConditionLock exit = new ConditionLock(new NotifyAllBooleanLock());

    private final ConditionLock pause = new ConditionLock(new NotifyAllBooleanLock());

    private final ConditionLock wake = new ConditionLock(new NotifyAllBooleanLock());

    protected RepeatingThread(final String name)
    {
        this(name, null);
    }

    protected RepeatingThread(final String name, final Frequency frequency)
    {
        super(name);
        this.frequency = frequency;
    }

    public Frequency frequency()
    {
        return frequency;
    }

    public void frequency(final Frequency frequency)
    {
        this.frequency = frequency;
    }

    public boolean isPaused()
    {
        return pause.isSatisfied();
    }

    @Override
    public void pause()
    {
        if (isRunning() && !isPaused())
        {
            trace("Pause requested");
            pause.request();
            pause.waitFor(true);
            trace("Paused");
        }
    }

    @Override
    public void resume()
    {
        if (!isRunning())
        {
            start();
        }
        else
        {
            if (isPaused())
            {
                trace("Resuming");
                pause.reset();
                trace("Resumed");
            }
        }
    }

    @Override
    public void run()
    {
        trace("Running");
        started();
        initialDelay();
        Frequency.Cycle cycle = null;
        if (frequency != null)
        {
            cycle = frequency.start(Time.now());
        }
        while (!exit.isRequested())
        {
            if (pause.isRequested())
            {
                trace("Pause request satisfied");
                pause.satisfy();
                pause.waitFor(false);
                trace("Unpaused");
            }
            if (!exit.isRequested())
            {
                onBefore();
                try
                {
                    onRun();
                }
                catch (final Throwable e)
                {
                    problem(e, "${class} threw exception", getClass());
                }
                onAfter();
                if (cycle != null)
                {
                    wake.waitFor(true, cycle.untilNext());
                }
            }
        }
        trace("Exiting");
        exited();
        exit.satisfy();
    }

    @Override
    public void stop(final Duration wait)
    {
        if (isPaused())
        {
            trace("Resuming paused thread to stop it");
            resume();
        }
        if (isRunning())
        {
            trace("Stopping");

            // Request that the thread exit
            exit.request();

            // It's time for the thread to wake up
            wake.satisfy();

            // Interrupt it in case it's sleeping
            interrupt();

            // Then wait for it to exit
            if (exit.waitFor(true, wait))
            {
                trace("Stopped");
            }
            else
            {
                trace("Thread did not exit after ${debug}", wait);
            }
        }
        else
        {
            trace("Already stopped");
        }
    }

    @Override
    protected abstract void onRun();
}
