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

package com.telenav.kivakit.core.thread;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.interfaces.lifecycle.Pausable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.PAUSED;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.PAUSE_REQUESTED;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.RESUME_REQUESTED;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.RUNNING;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.STOP_REQUESTED;

/**
 * A thread that repeatedly executes the {@link #onRun()} method implementation at a given {@link Frequency}. The thread
 * can be paused and resumed between executions of the user code.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class RepeatingThread extends KivaKitThread implements Pausable
{
    /**
     * @return A started thread with the given name that will run the given code at the given frequency. Unlike
     * {@link KivaKitThread#repeat}, this thread can be paused and resumed.
     */
    public static RepeatingThread run(Listener listener,
                                      String name,
                                      Frequency every,
                                      Runnable code)
    {
        var thread = new RepeatingThread(listener, name, code).frequency(every);
        thread.start();
        return thread;
    }

    /**
     * @return A started thread with the given name that will run the given code repeatedly. Unlike
     * {@link KivaKitThread#repeat}, this thread can be paused and resumed.
     */
    public static RepeatingThread run(Listener listener,
                                      String name,
                                      Runnable code)
    {
        return run(listener, name, Frequency.CONTINUOUSLY, code);
    }

    private Frequency frequency;

    public RepeatingThread(Listener listener, String name, Runnable code)
    {
        super(name, code);
        listener.listenTo(this);
    }

    public RepeatingThread(Listener listener, String name)
    {
        super(name, null);
        listener.listenTo(this);
    }

    public RepeatingThread(Listener listener, String name, Frequency frequency)
    {
        super(name);
        listener.listenTo(this);
        this.frequency = frequency;
    }

    public Frequency frequency()
    {
        return frequency;
    }

    public RepeatingThread frequency(Frequency frequency)
    {
        this.frequency = frequency;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPaused()
    {
        return is(PAUSED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause()
    {
        trace("Pause requested");
        stateMachine().transition(RUNNING, PAUSE_REQUESTED, PAUSED, this::interrupt);
        trace("Paused");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume()
    {
        if (!isRunning())
        {
            start();
        }
        else
        {
            stateMachine().transition(PAUSED, RESUME_REQUESTED, RUNNING, this::interrupt);
        }
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Execution entrypoint
     */
    @Override
    public void run()
    {
        // Wait to run,
        onWaiting();

        // start running,
        onRunning();

        // and while we are not requested to stop,
        var cycle = frequency.start();
        while (!is(STOP_REQUESTED))
        {
            // if the thread should be paused
            if (is(PAUSE_REQUESTED))
            {
                // wait for it to be resumed,
                waitFor(RESUME_REQUESTED);
                transition(RUNNING);
            }

            try
            {
                // then run the user's code
                onRun();
            }
            catch (Throwable e)
            {
                problem(e, "${class} threw exception", getClass());
            }

            if (cycle != null)
            {
                // and pause before the next cycle,
                cycle.waitTimeBeforeNextCycle().sleep();
            }
        }

        // finally, notify that we're exiting
        onRan();

        // and that we have exited.
        onExited();
    }
}
