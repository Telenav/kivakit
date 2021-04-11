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

import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.threading.conditions.StateMachine;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.CREATED;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.EXITED;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.EXITING;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.RUNNING;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.STOP;
import static com.telenav.kivakit.core.kernel.language.threading.KivaKitThread.State.WAITING;

@SuppressWarnings("UnusedReturnValue")
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class KivaKitThread extends BaseRepeater implements Startable, Runnable, Stoppable, Named
{
    /** Set of all KivaKit thread names */
    private static final Set<String> names = new HashSet<>();

    /**
     * @return A started thread with the given name that will run the given code at the given frequency.
     */
    public static KivaKitThread repeat(final Listener listener,
                                       final String name,
                                       final Frequency every,
                                       final Runnable code)
    {
        return run(listener, name, () -> every.duration().loop(listener, code));
    }

    /**
     * @return A started thread with the given name that has been started
     */
    public static KivaKitThread run(final Listener listener,
                                    final String name,
                                    final Runnable code)
    {
        final var thread = listener.listenTo(new KivaKitThread(name, code));
        thread.start();
        return thread;
    }

    /**
     * The execution states a thread can be in
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public enum State
    {
        /** The thread has been created but is not yet running */
        CREATED,

        /** The thread is waiting to run due to an {@link #waitForInitialDelayPeriod()} */
        WAITING,

        /** The thread is running */
        RUNNING,

        /** The {@link RepeatingKivaKitThread} has been requested to pause after its current cycle completes */
        PAUSE,

        /** The {@link RepeatingKivaKitThread} is paused between repeats */
        PAUSED,

        /** The thread is paused and should resume running */
        RESUME,

        /** The thread has been asked to stop */
        STOP,

        /** The thread is exiting */
        EXITING,

        /** The thread has stopped and exited */
        EXITED
    }

    /** The thread */
    private final Thread thread;

    /** The current state of this thread */
    private final StateMachine<State> state = new StateMachine<>(CREATED, state -> trace(name() + ": " + state.name()));

    /** Any initial delay before the thread starts running */
    private Duration initialDelay = Duration.NONE;

    private boolean initialized;

    /** The time at which this thread was started */
    private Time startedAt;

    /** The code to run, if any. If this value is null, then {@link #onRun()} is used instead */
    private Runnable code;

    /**
     * Creates a daemon thread with the given name prefixed by "Kiva-" so it is easy to distinguish from other threads.
     *
     * @param name The thread name suffix
     * @param code The code to run
     */
    public KivaKitThread(final String name, final Runnable code)
    {
        this(name);
        this.code = code;
    }

    /**
     * Creates a daemon thread with the given name prefixed by "Kiva-" so it is easy to distinguish from other threads.
     *
     * @param name The thread name suffix
     */
    public KivaKitThread(final String name)
    {
        super(name("Kiva-" + name));
        thread = new Thread(this, objectName());
        thread.setDaemon(true);
    }

    /**
     * @param daemon True to make this a daemon thread
     */
    public KivaKitThread daemon(final boolean daemon)
    {
        thread.setDaemon(daemon);
        return this;
    }

    /**
     * Makes this thread high priority
     */
    public KivaKitThread highPriority()
    {
        thread.setPriority(Thread.MAX_PRIORITY);
        return this;
    }

    /**
     * Sets an initial delay before the thread's user code starts executing
     */
    public KivaKitThread initialDelay(final Duration initialDelay)
    {
        this.initialDelay = initialDelay;
        return this;
    }

    /**
     * Interrupts this thread if it is asleep or waiting
     */
    public KivaKitThread interrupt()
    {
        thread.interrupt();
        return this;
    }

    /**
     * @return True if this thread is in the {@link State#RUNNING} state
     */
    @Override
    public boolean isRunning()
    {
        return state().is(RUNNING);
    }

    /**
     * Waits until this thread exits
     */
    public void join()
    {
        try
        {
            thread.join();
        }
        catch (final InterruptedException ignored)
        {
        }
    }

    /**
     * Makes this a low priority thread
     */
    public KivaKitThread lowPriority()
    {
        thread.setPriority(Thread.MIN_PRIORITY);
        return this;
    }

    /**
     * @return This thread's name
     */
    @Override
    public String name()
    {
        return thread.getName();
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

        try
        {
            // run the user's code
            onRun();
        }
        catch (final Throwable e)
        {
            problem(e, "${class} threw exception", getClass());
        }

        // then notify that we're exiting
        onExiting();

        // and that we have exited.
        onExited();
    }

    /**
     * @return True if this thread should stop now
     */
    public boolean shouldStop()
    {
        return state().is(STOP);
    }

    /**
     * Starts this thread if it is not already running
     */
    @Override
    public boolean start()
    {
        if (!isRunning())
        {
            try
            {
                thread.start();
            }
            catch (final IllegalThreadStateException e)
            {
                warning(e, "Thread could not be started");
                return false;
            }
        }
        return true;
    }

    /**
     * Starts this thread but doesn't return until it is running
     */
    public KivaKitThread startSynchronously()
    {
        start();
        waitFor(RUNNING);
        return this;
    }

    /**
     * @return The time at which this thread started
     */
    public Time startedAt()
    {
        return startedAt;
    }

    /**
     * @return The state that this thread is in
     */
    public StateMachine<State> state()
    {
        return state;
    }

    /**
     * Attempt to stop this thread, waiting for the maximum specified time for it to exit
     */
    @Override
    public void stop(final Duration maximumWait)
    {
        state().transitionTo(STOP);
        waitFor(EXITED, maximumWait);
    }

    /**
     * Wait for this thread to achieve the given states
     */
    public void waitFor(final State state)
    {
        state().waitFor(state);
    }

    /**
     * Wait for this thread to achieve the given states
     */
    public void waitFor(final State state, final Duration maximumWait)
    {
        state().waitFor(state, maximumWait);
    }

    /**
     * Called when this thread exits
     */
    @MustBeInvokedByOverriders
    protected void onExited()
    {
        state().transitionTo(EXITED);
    }

    @MustBeInvokedByOverriders
    protected void onExiting()
    {
        state().transitionTo(EXITING);
    }

    /**
     * Called if there is no {@link Runnable} code provided to the constructor
     */
    protected void onRun()
    {
        ensureNotNull(code, "Must either provide code to thread constructor or implement onRun()");
        code.run();
    }

    /**
     * Called when this thread is running
     */
    @MustBeInvokedByOverriders
    protected void onRunning()
    {
        startedAt = Time.now();
        state().transitionTo(RUNNING);
    }

    /**
     * Called when this thread is waiting to run
     */
    @MustBeInvokedByOverriders
    protected void onWaiting()
    {
        state().transitionTo(WAITING);
        waitForInitialDelayPeriod();
    }

    /**
     * @return The thread object for this KivaKit thread
     */
    protected Thread thread()
    {
        return thread;
    }

    /**
     * Waits for any prescribed initial delay
     */
    protected void waitForInitialDelayPeriod()
    {
        if (!initialized)
        {
            initialDelay.sleep();
            initialized = true;
        }
    }

    private static String name(String name)
    {
        int number = 1;
        while (names.contains(name))
        {
            name = name + "-" + number++;
        }
        names.add(name);
        return name;
    }
}
