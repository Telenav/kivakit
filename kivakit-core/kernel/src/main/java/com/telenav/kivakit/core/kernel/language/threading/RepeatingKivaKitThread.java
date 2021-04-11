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

package com.telenav.kivakit.core.kernel.language.threading;

import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Pausable;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.PAUSED;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.PAUSE_REQUESTED;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.RESUME_REQUESTED;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.RUNNING;

/**
 * A thread that repeatedly executes the {@link #onRun()} method implementation at a given {@link Frequency}. The thread
 * can be paused and resumed between executions of the user code.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class RepeatingKivaKitThread extends KivaKitThread implements Pausable
{
    /**
     * @return A started thread with the given name that will run the given code at the given frequency. Unlike {@link
     * KivaKitThread#repeat}, this thread can be paused and resumed.
     */
    public static RepeatingKivaKitThread run(final Listener listener,
                                             final String name,
                                             final Frequency every,
                                             final Runnable code)
    {
        final var thread = new RepeatingKivaKitThread(listener, name, code).frequency(every);
        thread.start();
        return thread;
    }

    /**
     * @return A started thread with the given name that will run the given code repeatedly. Unlike {@link
     * KivaKitThread#repeat}, this thread can be paused and resumed.
     */
    public static RepeatingKivaKitThread run(final Listener listener,
                                             final String name,
                                             final Runnable code)
    {
        return run(listener, name, Frequency.CONTINUOUSLY, code);
    }

    private Frequency frequency;

    public RepeatingKivaKitThread(final Listener listener, final String name, final Runnable code)
    {
        super(name, code);
        listener.listenTo(this);
    }

    public RepeatingKivaKitThread(final Listener listener, final String name, final Frequency frequency)
    {
        super(name);
        listener.listenTo(this);
        this.frequency = frequency;
    }

    public Frequency frequency()
    {
        return frequency;
    }

    public RepeatingKivaKitThread frequency(final Frequency frequency)
    {
        this.frequency = frequency;
        return this;
    }

    public boolean isPaused()
    {
        return state().is(PAUSED);
    }

    @Override
    public void pause()
    {
        trace("Pause requested");
        state().transitionAndWait(RUNNING, PAUSE_REQUESTED, PAUSED);
        trace("Paused");
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
            state().transitionAndWait(PAUSED, RESUME_REQUESTED, RUNNING);
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

        final var cycle = frequency.start();

        while (!shouldStop())
        {
            if (state().is(PAUSE_REQUESTED))
            {
                state().waitFor(RESUME_REQUESTED);
            }

            if (!shouldStop())
            {
                try
                {
                    // run the user's code
                    onRun();
                }
                catch (final Throwable e)
                {
                    problem(e, "${class} threw exception", getClass());
                }

                if (cycle != null)
                {
                    cycle.waitTimeBeforeNextCycle().sleep();
                }
            }
        }

        // then notify that we're exiting
        onExiting();

        // and that we have exited.
        onExited();
    }
}
