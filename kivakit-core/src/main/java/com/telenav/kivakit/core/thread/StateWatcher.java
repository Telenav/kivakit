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

import com.telenav.kivakit.core.lexakai.DiagramThread;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.core.thread.locks.Lock;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.interfaces.time.WakeState;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.function.Predicate;

import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.interfaces.time.WakeState.COMPLETED;
import static com.telenav.kivakit.interfaces.time.WakeState.INTERRUPTED;
import static com.telenav.kivakit.interfaces.time.WakeState.TERMINATED;
import static com.telenav.kivakit.interfaces.time.WakeState.TIMED_OUT;

/**
 * Allows a thread to wait for a particular state or for predicate to be satisfied by some other thread calling {@link
 * #signal(Object)}.
 *
 * <p>
 * A thread waiting for a particular value calls one of:
 * </p>
 *
 * <ul>
 *     <li>{@link #waitFor(Object)}</li>
 *     <li>{@link #waitFor(Object, Duration)}</li>
 * </ul>
 *
 * <p>
 * A thread waiting for a predicate to be satisfied by a value calls one of:
 * </p>
 *
 * <ul>
 *     <li>{@link #waitFor(Predicate)}</li>
 *     <li>{@link #waitFor(Predicate, Duration)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
@LexakaiJavadoc(complete = true)
public final class StateWatcher<State>
{
    /**
     * A thread that is waiting for its predicate to be satisfied
     */
    @LexakaiJavadoc(complete = true)
    private class Waiter
    {
        private Waiter(Predicate<State> predicate, Condition condition)
        {
            this.predicate = predicate;
            this.condition = condition;
        }

        /** The predicate that must be satisfied */
        final Predicate<State> predicate;

        /** The condition variable to wait on and signal */
        final Condition condition;
    }

    /** The most recently reported state */
    private volatile State current;

    /** The re-entrant lock */
    private final Lock lock = new Lock();

    /** The clients waiting for a predicate to be satisfied */
    private final List<Waiter> waiters = new ArrayList<>();

    public StateWatcher(State current)
    {
        this.current = current;
    }

    /**
     * Signals any waiters if the state they are waiting for has arrived
     */
    public void signal(State state)
    {
        whileLocked(() ->
        {
            // Update the current state,
            current = state;

            // go through the waiters
            for (var watcher : waiters)
            {
                // and if the reported value satisfies the watcher's predicate,
                if (watcher.predicate.test(state))
                {
                    // signal it to wake up.
                    watcher.condition.signal();
                }
            }
        });
    }

    /**
     * Wait for the given boolean predicate to be true forever
     *
     * @see #waitFor(Predicate, Duration)
     */
    public WakeState waitFor(Predicate<State> predicate)
    {
        return waitFor(predicate, Duration.MAXIMUM);
    }

    /**
     * Waits for the given boolean predicate to become true based on changes to the observed value
     *
     * @param maximumWaitTime The maximum amount of time to wait before giving up ensure that the wait condition is
     * satisfied.
     */
    public WakeState waitFor(Predicate<State> predicate,
                             Duration maximumWaitTime)
    {
        var started = Time.now();
        return whileLocked(() ->
        {
            Waiter waiter = null;
            while (true)
            {
                // If the predicate is already satisfied,
                if (predicate.test(current))
                {
                    // we're done.
                    return COMPLETED;
                }

                // otherwise, add ourselves as a waiter,
                if (waiter == null)
                {
                    waiter = new Waiter(predicate, lock.newCondition());
                    waiters.add(waiter);
                }

                // and go to sleep until our condition is satisfied or half a second elapses
                // (we wait only a short time as a defensive measure to avoid hangs)
                var wake = seconds(0.5).await(waiter.condition::await);
                if (wake == TERMINATED || wake == INTERRUPTED)
                {
                    return wake;
                }
                if (started.elapsedSince().isGreaterThan(maximumWaitTime))
                {
                    return TIMED_OUT;
                }
            }
        });
    }

    /**
     * Wait for the desired state to be reached
     *
     * @param desired The desired state
     * @param maximumWaitTime The maximum amount of time to wait
     */
    public WakeState waitFor(State desired, Duration maximumWaitTime)
    {
        return waitFor(desired::equals, maximumWaitTime);
    }

    /**
     * Wait forever for the desired state
     */
    public WakeState waitFor(State desired)
    {
        return waitFor(desired, Duration.MAXIMUM);
    }

    /**
     * Executes the given code while holding this object's reentrant lock
     */
    public void whileLocked(Runnable code)
    {
        lock.whileLocked(code);
    }

    /**
     * Executes the given code while holding this object's reentrant lock
     */
    public <T> T whileLocked(Code<T> code)
    {
        return lock.whileLocked(code);
    }
}
