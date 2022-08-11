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

import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.interfaces.lifecycle.Pausable;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.time.WakeState;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.thread.KivaKitThread.State.CREATED;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.EXITED;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.RAN;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.RUNNING;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.STOP_REQUESTED;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.WAITING;

/**
 * A thread with methods to start, pause, resume and stop (pause and resume are implemented only in
 * {@link RepeatingThread}). KivaKit threads have a uniform naming convention with each thread name prefixed with
 * "KivaKit-". {@link KivaKitThread}s are {@link Repeater}s and can broadcast messages to interested {@link Listener}s.
 *
 * <p><b>Thread Lifecycle States</b></p>
 *
 * <p>
 * During the lifecycle of a KivaKit thread, it transitions from one {@link State} to another as code executes and
 * methods are called to control execution. These states are managed with a {@link StateMachine}, which enables state
 * transitions and allows specific states to be waited on. The method {@link #stateMachine()} gives access to the
 * {@link StateMachine} and the convenience methods {@link #waitFor(State)} or {@link #waitFor(State, Duration)} allow
 * states to be waited for. In lifecycle-order, thread states are:
 * </p>
 *
 * <ul>
 *     <li>{@link State#CREATED} - This thread has been created but is not yet waiting</li>
 *     <li>{@link State#WAITING} - This thread is waiting for the prescribed initial delay before running</li>
 *     <li>{@link State#RUNNING} - This thead is now running user code</li>
 *     <li>{@link State#PAUSE_REQUESTED} - This thread has been asked to pause</li>
 *     <li>{@link State#PAUSED} - This thread is paused</li>
 *     <li>{@link State#RESUME_REQUESTED} - This thread has been asked to resume</li>
 *     <li>{@link State#STOP_REQUESTED} - This thread has been requested to stop</li>
 *     <li>{@link State#RAN} - This thread is about to exit</li>
 *     <li>{@link State#EXITED} - This thread has exited</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #daemon(boolean)} - Makes this thread a daemon if true, must be set before the thread is started</li>
 *     <li>{@link #highPriority()} - Makes this thread high priority</li>
 *     <li>{@link #initialDelay(Duration)} - Sets a delay that should pass before this thread starts to execute user code</li>
 *     <li>{@link #lowPriority()} - Makes this thread low priority</li>
 *     <li>{@link #startedAt()} - The time at which this thread transitioned to {@link State#RUNNING}</li>
 *     <li>{@link #stateMachine()} - The current state of the thread as a {@link StateMachine} that can be waited on</li>
 *     <li>{@link #thread()} - The underlying Java thread object</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #is(State)} - True if this thread is in the given {@link State}</li>
 *     <li>{@link #isRunning()} - True if this thread is in {@link State#RUNNING}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #start()} - Starts this thread, transitioning it to {@link State#WAITING} and then {@link State#RUNNING}</li>
 *     <li>{@link #startSynchronously()} - Starts this thread and waits for it to reach {@link State#RUNNING}</li>
 *     <li>{@link #interrupt()} - Interrupts this thread</li>
 *     <li>{@link #stop()} - Asks this thread to stop by transitioning to the {@link State#STOP_REQUESTED} state.
 *         When user code checks this value, it should return, causing the thread to exit. </li>
 *     <li>{@link #stop(Duration)} - Asks this thread to stop and waits for up to the given duration for it to reach {@link State#EXITED}</li>
 *     <li>{@link #waitFor(State)} - Waits for the given {@link State}</li>
 *     <li>{@link #waitFor(State, Duration)} - Waits for up to the given maximum duration for this thread to reach the given {@link State}</li>
 * </ul>
 *
 * <p><b>Overrides</b></p>
 *
 * <ul>
 *     <li>{@link #onWaiting()} - Called when a thread is started and is in {@link State#WAITING}</li>
 *     <li>{@link #onRun()} - Overridden to provide user code to execute if {@link #KivaKitThread(String, Runnable)}
 *     was not called to provide user code</li>
 *     <li>{@link #onRunning()} - Called when a thread transitions to {@link State#RUNNING}</li>
 *     <li>{@link #onRan()} - Called when a thread reaches {@link State#RAN} and is about to exit</li>
 *     <li>{@link #onExited()} - Called when a thread has reached {@link State#EXITED}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Startable
 * @see Runnable
 * @see Pausable
 * @see Stoppable
 * @see Repeater
 */
@SuppressWarnings("UnusedReturnValue")
@UmlClassDiagram(diagram = DiagramThread.class)
@LexakaiJavadoc(complete = true)
public class KivaKitThread extends BaseRepeater implements
        Startable,
        Runnable,
        Stoppable<Duration>,
        Named
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** Set of all KivaKit thread names */
    private static final Set<String> names = new HashSet<>();

    /**
     * @return A started thread with the given name that will run the given code at the given frequency.
     */
    public static KivaKitThread repeat(String name,
                                       Frequency every,
                                       UncheckedVoidCode code)
    {
        return repeat(LOGGER, name, every, code);
    }

    /**
     * @return A started thread with the given name that will run the given code at the given frequency.
     */
    public static KivaKitThread repeat(Listener listener,
                                       String name,
                                       Frequency every,
                                       UncheckedVoidCode code)
    {
        return run(listener, name, () -> code.loop(listener, every.cycleLength()));
    }

    /**
     * @return A started thread with the given name that has been started
     */
    public static KivaKitThread run(String name,
                                    Runnable code)
    {
        return run(LOGGER, name, code);
    }

    /**
     * @return A started thread with the given name that has been started
     */
    public static KivaKitThread run(Listener listener,
                                    String name,
                                    Runnable code)
    {
        var thread = listener.listenTo(new KivaKitThread(name, code));
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

        /** The {@link RepeatingThread} has been requested to pause after its current cycle completes */
        PAUSE_REQUESTED,

        /** The {@link RepeatingThread} is paused between repeats */
        PAUSED,

        /** The thread is paused and should resume running */
        RESUME_REQUESTED,

        /** The thread has been asked to stop */
        STOP_REQUESTED,

        /** The thread has finished running */
        RAN,

        /** The thread has stopped and exited */
        EXITED
    }

    /** The code to run, if any. If this value is null, then {@link #onRun()} is used instead */
    private final Runnable code;

    /** Any initial delay before the thread starts running */
    private Duration initialDelay = Duration.ZERO_DURATION;

    /** The time at which this thread was started */
    private Time startedAt;

    /** The thread */
    private final Thread thread;

    /** The current state of this thread */
    private final StateMachine<State> stateMachine = listenTo(new StateMachine<>(CREATED, state -> trace(name() + ": " + state.name())));

    /**
     * Creates a daemon thread with the given name prefixed by "Kiva-" so it is easy to distinguish from other threads.
     *
     * @param name The thread name suffix
     * @param code The code to run
     */
    public KivaKitThread(String name, Runnable code)
    {
        super(name("Kiva-" + name));

        thread = new Thread(this, objectName());
        thread.setDaemon(true);

        this.code = code;
    }

    /**
     * Creates a daemon thread with the given name prefixed by "Kiva-" so it is easy to distinguish from other threads.
     *
     * @param name The thread name suffix
     */
    public KivaKitThread(String name)
    {
        this(name, () ->
        {
        });
    }

    /**
     * @param daemon True to make this a daemon thread
     */
    public KivaKitThread daemon(boolean daemon)
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
    public KivaKitThread initialDelay(Duration initialDelay)
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
     * @return True if this thread is in the given state at the time this method is called.
     */
    public boolean is(State state)
    {
        return stateMachine().is(state);
    }

    /**
     * @return True if this thread is in the {@link State#RUNNING} state
     */
    @Override
    public boolean isRunning()
    {
        return is(RUNNING);
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
        catch (InterruptedException ignored)
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

    @Override
    public Duration maximumWaitTime()
    {
        return Duration.MAXIMUM;
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
        catch (Throwable e)
        {
            problem(e, "${class} threw exception", getClass());
        }

        // then notify that we're done running
        onRan();

        // and that we have exited.
        onExited();
    }

    /**
     * Starts this thread if it is not already running
     */
    @Override
    public boolean start()
    {
        return whileLocked(() ->
        {
            if (!isRunning())
            {
                try
                {
                    startedAt = Time.now();
                    trace("Starting");
                    thread.start();
                }
                catch (IllegalThreadStateException e)
                {
                    warning(e, "Thread could not be started");
                    return false;
                }
            }
            return true;
        });
    }

    /**
     * Starts this thread but doesn't return until it is running
     */
    public KivaKitThread startSynchronously()
    {
        start();
        stateMachine().waitForNot(WAITING);
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
    public StateMachine<State> stateMachine()
    {
        return stateMachine;
    }

    /**
     * Attempt to stop this thread, waiting for the maximum specified time for it to exit
     */
    @Override
    public void stop(Duration maximumWait)
    {
        whileLocked(() ->
        {
            if (!is(EXITED))
            {
                transition(STOP_REQUESTED);
                interrupt();
                waitFor(EXITED, maximumWait);
            }
        });
    }

    /**
     * Wait for this thread to achieve the given states
     */
    public WakeState waitFor(State state)
    {
        trace("Wait for $", state);
        return stateMachine().waitFor(state);
    }

    /**
     * Wait for this thread to achieve the given states
     */
    public WakeState waitFor(State state, Duration maximumWait)
    {
        trace("Wait for $", state);
        return stateMachine().waitFor(state, maximumWait);
    }

    /**
     * Executes the given code while holding the state machine's reentrant lock
     */
    public void whileLocked(Runnable code)
    {
        stateMachine().whileLocked(code);
    }

    /**
     * Executes the given code while holding the state machine's reentrant lock
     */
    public <T> T whileLocked(Code<T> code)
    {
        return stateMachine().whileLocked(code);
    }

    /**
     * Called when this thread exits
     */
    @MustBeInvokedByOverriders
    protected void onExited()
    {
        transition(EXITED);
    }

    @MustBeInvokedByOverriders
    protected void onRan()
    {
        transition(RAN);
    }

    /**
     * Called if there is no {@link Runnable} code provided to the constructor
     */
    protected void onRun()
    {
        Ensure.ensureNotNull(code, "Must either provide code to thread constructor or override onRun()");
        code.run();
    }

    /**
     * Called when this thread is running
     */
    @MustBeInvokedByOverriders
    protected void onRunning()
    {
        transition(RUNNING);
    }

    /**
     * Called when this thread is waiting to run
     */
    @MustBeInvokedByOverriders
    protected void onWaiting()
    {
        transition(WAITING);
        waitForInitialDelayPeriod();
    }

    /**
     * @return The thread object for this KivaKit thread
     */
    protected Thread thread()
    {
        return thread;
    }

    protected State transition(State to)
    {
        return stateMachine().transitionTo(to);
    }

    /**
     * Waits for any prescribed initial delay
     */
    protected void waitForInitialDelayPeriod()
    {
        initialDelay.sleep();
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
