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

package com.telenav.kivakit.kernel.language.threading.conditions;

import com.telenav.kivakit.kernel.interfaces.code.Code;
import com.telenav.kivakit.kernel.language.threading.locks.Lock;
import com.telenav.kivakit.kernel.language.threading.status.WakeState;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.function.Predicate;

import static com.telenav.kivakit.kernel.language.threading.status.WakeState.COMPLETED;
import static com.telenav.kivakit.kernel.language.threading.status.WakeState.INTERRUPTED;
import static com.telenav.kivakit.kernel.language.threading.status.WakeState.TIMED_OUT;

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
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
@LexakaiJavadoc(complete = true)
public class StateWatcher<State>
{
    /**
     * A thread that is waiting for its predicate to be satisfied
     */
    @LexakaiJavadoc(complete = true)
    private class Waiter
    {
        /** The predicate that must be satisfied */
        Predicate<State> predicate;

        /** The condition variable to wait on and signal */
        Condition condition;
    }

    /** The re-entrant lock */
    final transient Lock lock = new Lock();

    /** The clients waiting for a predicate to be satisfied */
    private final List<Waiter> waiters = new ArrayList<>();

    /** The most recently reported state */
    private volatile State current;

    public StateWatcher(final State current)
    {
        this.current = current;
    }

    /**
     * Signals any waiters if the state they are waiting for has arrived
     */
    public void signal(final State state)
    {
        whileLocked(() ->
        {
            // Update the current state,
            current = state;

            // go through the waiters
            for (final var watcher : waiters)
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
    public WakeState waitFor(final Predicate<State> predicate)
    {
        return waitFor(predicate, Duration.MAXIMUM);
    }

    /**
     * Waits for the given boolean predicate to become true based on changes to the observed value
     *
     * @param maximumWaitTime The maximum amount of time to wait before giving up ensure that the wait condition is
     * satisfied.
     */
    public WakeState waitFor(final Predicate<State> predicate,
                             final Duration maximumWaitTime)
    {
        return whileLocked(() ->
        {
            // If the predicate is already satisfied,
            if (predicate.test(current))
            {
                // we're done.
                return COMPLETED;
            }

            // otherwise, add ourselves as a waiter,
            final var waiter = new Waiter();
            waiter.predicate = predicate;
            waiter.condition = lock.newCondition();
            waiters.add(waiter);

            try
            {
                // and go to sleep until our condition is satisfied
                if (waiter.condition.await(maximumWaitTime.asMilliseconds(), TimeUnit.MILLISECONDS))
                {
                    return TIMED_OUT;
                }
                else
                {
                    return COMPLETED;
                }
            }
            catch (final InterruptedException e)
            {
                return INTERRUPTED;
            }
        });
    }

    /**
     * Wait for the desired state to be reached
     *
     * @param desired The desired state
     * @param maximumWaitTime The maximum amount of time to wait
     */
    public WakeState waitFor(final State desired, final Duration maximumWaitTime)
    {
        return waitFor(desired::equals, maximumWaitTime);
    }

    /**
     * Wait forever for the desired state
     */
    public WakeState waitFor(final State desired)
    {
        return waitFor(desired, Duration.MAXIMUM);
    }

    /**
     * Executes the given code while holding this object's reentrant lock
     */
    public void whileLocked(final Runnable code)
    {
        lock.whileLocked(code);
    }

    /**
     * Executes the given code while holding this object's reentrant lock
     */
    public <T> T whileLocked(final Code<T> code)
    {
        return lock.whileLocked(code);
    }
}
