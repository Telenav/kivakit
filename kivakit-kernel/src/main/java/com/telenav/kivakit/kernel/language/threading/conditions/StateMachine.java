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
import com.telenav.kivakit.kernel.interfaces.messaging.Receiver;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A simple state machine with a single state that can be transitioned from one state to another and which allows a
 * caller to wait for the machine to transition to a particular state.
 * <p>
 * <b>Example</b>
 * <pre>
 * enum State { UNBORN, LIVING, DEAD }
 *
 *     [...]
 *
 * var person = new StateMachine(State.UNBORN);
 *
 *     [...]
 *
 * KivaKitThread.run("Life", () -&gt;
 * {
 *     // Get born
 *     person.transitionTo(LIVING);
 *
 *     // live life
 *     while (person.is(LIVING))
 *     {
 *         doSomething();
 *     }
 *
 *     // then exit.
 * });
 *
 * KivaKitThread.run("GrimReaper", () -&gt;
 * {
 *     // Wait a while
 *     Duration.years(random(101)).sleep();
 *
 *     // then kill the person
 *     person.transitionTo(DEAD);
 * });
 *
 * KivaKitThread.run("FuneralHome", () -&gt;
 * {
 *     // Wait for some business
 *     person.waitFor(DEAD);
 *
 *     // then make some money.
 *     cashIn();
 * });
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see StateWatcher
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
public final class StateMachine<State> extends BaseRepeater
{
    /** The current state of the receiver */
    private State at;

    /** Watches the state */
    private final StateWatcher<State> watcher;

    /** Listener to call when state transitions occur */
    private final Receiver<State> receiver;

    public StateMachine(final State initial)
    {
        this(initial, null);
    }

    public StateMachine(final State initial, final Receiver<State> receiver)
    {
        at = initial;
        this.receiver = receiver;
        watcher = new StateWatcher<>(initial);
    }

    public State at()
    {
        return at;
    }

    /**
     * @return True if the current state is the given state
     */
    public boolean is(final State state)
    {
        return whileLocked(() -> Objects.equals(at, state));
    }

    /**
     * @return True if the current state is the given state
     */
    public boolean is(final Predicate<State> predicate)
    {
        return whileLocked(() -> predicate.test(at));
    }

    /**
     * @return True if the current state is the given state
     */
    public boolean isNot(final Predicate<State> predicate)
    {
        return whileLocked(() -> !predicate.test(at));
    }

    /**
     * @return True if the current state is not the given state
     */
    public boolean isNot(final State state)
    {
        return whileLocked(() -> !Objects.equals(at, state));
    }

    /**
     * Toggles between two states by transitioning from one to the other
     *
     * @param one The first state
     * @param two The second state
     * @return The previous state, or null if toggling is not possible because this state machine isn't in either state
     */
    public Optional<State> toggle(final State one, final State two)
    {
        if (is(one))
        {
            return Optional.of(transitionTo(two));
        }
        if (is(two))
        {
            return Optional.of(transitionTo(one));
        }
        return Optional.empty();
    }

    /**
     * If we are in the 'from' state, transition to the 'to' state.
     *
     * @param from The expected state
     * @param to The desired state
     * @return True if a state change occurred
     */
    public boolean transition(final State from, final State to)
    {
        return whileLocked(() ->
        {
            if (is(from))
            {
                transitionTo(to);
                return true;
            }

            return false;
        });
    }

    /**
     * If the state is equal to the 'from' state, transition to the 'to' state and then wait for the 'waitFor' state. An
     * example use case is to transition from RUNNING to STOPPING and then wait for STOPPED.
     *
     * @param from The expected initial state
     * @param to The state to transition to
     * @param waitFor The state to wait for if the transition is successful
     * @param before Code to run while holding the state machine lock but before attempting to transition to a new
     * state. This code might be used to interrupt a thread, for example.
     * @return True if desired state was reached
     */
    public boolean transition(final State from,
                              final State to,
                              final State waitFor,
                              final Duration maximumWait,
                              final Runnable before)
    {
        return whileLocked(() ->
        {
            // If we are already in the desired state,
            if (is(waitFor))
            {
                // then we are already successful.
                return true;
            }

            // Run the user code (perhaps interrupting a thread),
            before.run();

            // and if we can transition from the 'from' state to the 'to' state,
            if (transition(from, to))
            {
                // then wait for the desired state to arrive.
                trace("$ => $ (wait for $ for up to $)", from, to, waitFor, maximumWait);
                waitFor(waitFor, maximumWait);
                return true;
            }

            // We were not in the 'from' state.
            return false;
        });
    }

    /**
     * Transition and wait forever for the given state
     *
     * @see #transition(Object, Object, Object, Duration, Runnable)
     */
    public synchronized boolean transition(final State from,
                                           final State to,
                                           final State waitFor,
                                           final Runnable before)
    {
        return transition(from, to, waitFor, Duration.MAXIMUM, before);
    }

    public void transitionAndWaitForNot(final State state)
    {
        whileLocked(() ->
        {
            transitionTo(state);
            waitForNot(state);
        });
    }

    /**
     * Transitions to the given state regardless of the current state
     *
     * @return The prior state
     */
    public State transitionTo(final State state)
    {
        return whileLocked(() ->
        {
            // Get the current state,
            final var prior = at;

            // update the current state,
            at = state;
            trace("$ => $", prior, at);

            // signal any waiting threads that there is a new state,
            watcher.signal(at);

            // and if there is a state receiver,
            if (receiver != null)
            {
                // notify it.
                receiver.receive(at);
            }

            return prior;
        });
    }

    /**
     * Waits forever for the given state to be achieved
     *
     * @param state The state to wait for
     * @return True if the state was achieved, false if the operation timed out
     */
    public boolean waitFor(final State state)
    {
        return waitFor(state, Duration.MAXIMUM);
    }

    /**
     * Waits until this state machine reaches the given state
     *
     * @param state The desired state
     * @param maximumWait The maximum amount of time to wait
     * @return True if the state was achieved, false if the operation timed out
     */
    public boolean waitFor(final State state, final Duration maximumWait)
    {
        return waitFor(ignored -> is(state), maximumWait);
    }

    /**
     * Waits until the given predicate is satisfied
     *
     * @return True if the state was achieved, false if the operation timed out
     */
    public boolean waitFor(final Predicate<State> predicate)
    {
        return waitFor(predicate, Duration.MAXIMUM);
    }

    /**
     * Waits until the given predicate is satisfied
     *
     * @param predicate The desired state
     * @param maximumWait The maximum amount of time to wait
     * @return True if the state was achieved, false if the operation timed out
     */
    public boolean waitFor(final Predicate<State> predicate, final Duration maximumWait)
    {
        return waitFor(predicate, maximumWait, () ->
        {
        });
    }

    /**
     * Waits until the given predicate is satisfied
     *
     * @param predicate The desired state
     * @param maximumWait The maximum amount of time to wait
     * @param beforeWaiting Code to run after locking the state machine but before waiting for the predicate. This code
     * might interrupt another thread, for example.
     * @return True if the state was achieved, false if the operation timed out
     */
    public boolean waitFor(final Predicate<State> predicate, final Duration maximumWait, final Runnable beforeWaiting)
    {
        return whileLocked(() ->
        {
            beforeWaiting.run();
            watcher.waitFor(predicate, maximumWait);
            return predicate.test(at);
        });
    }

    /**
     * Waits until this state machine is NOT in the given state
     *
     * @param state The state this machine should NOT be in
     * @return True if the desired state was achieved, false if the operation timed out
     */
    public boolean waitForNot(final State state)
    {
        return waitForNot(state, Duration.MAXIMUM);
    }

    /**
     * Waits until this state machine is NOT in the given state
     *
     * @param state The state this machine should NOT be in
     * @param maximumWait The maximum amount of time to wait
     * @return True if the desired state was achieved, false if the operation timed out
     */
    public boolean waitForNot(final State state, final Duration maximumWait)
    {
        return waitFor(ignored -> !is(state), maximumWait);
    }

    /**
     * Executes the given code while holding the state watcher's reentrant lock
     */
    public void whileLocked(final Runnable code)
    {
        watcher.whileLocked(code);
    }

    /**
     * Executes the given code while holding the state watcher's reentrant lock
     */
    public <T> T whileLocked(final Code<T> code)
    {
        return watcher.whileLocked(code);
    }
}
