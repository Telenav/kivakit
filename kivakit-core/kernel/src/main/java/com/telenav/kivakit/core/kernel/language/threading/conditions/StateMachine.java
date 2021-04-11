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

package com.telenav.kivakit.core.kernel.language.threading.conditions;

import com.telenav.kivakit.core.kernel.interfaces.messaging.Receiver;
import com.telenav.kivakit.core.kernel.language.threading.status.WakeState;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
public class StateMachine<State> extends BaseRepeater
{
    /** The current state of the receiver */
    private final AtomicReference<State> state = new AtomicReference<>();

    /** Watches the state */
    private final StateWatcher<State> waiter = new StateWatcher<>();

    /** Listener to call when state transitions occur */
    private final Receiver<State> receiver;

    public StateMachine(final State initial)
    {
        this(initial, null);
    }

    public StateMachine(final State initial, final Receiver<State> receiver)
    {
        this.receiver = receiver;
        state.set(initial);
    }

    /**
     * @return True if the current state is the given state
     */
    public boolean is(final State state)
    {
        return Objects.equals(this.state.get(), state);
    }

    /**
     * If the state is equal to the 'from' state, transition to the 'to' state.
     *
     * @param from The expected state
     * @param to The desired state
     * @return True if the state transitioned
     */
    public boolean transition(final State from, final State to)
    {
        // Transition to the new state,
        final var transitioned = state.compareAndSet(from, to);

        // and if we did transition,
        if (transitioned)
        {
            // show the state transition,
            trace("$ => $", from, to);

            // and transmit
            stateChanged(to);
        }

        return transitioned;
    }

    /**
     * Transition and wait forever for the given state
     *
     * @see #transitionAndWait(Object, Object, Object, Duration)
     */
    public boolean transitionAndWait(final State from, final State to, final State waitFor)
    {
        return transitionAndWait(from, to, waitFor, Duration.MAXIMUM);
    }

    /**
     * If the state is equal to the 'from' state, transition to the 'to' state and then wait for the 'waitFor' state. An
     * example use case is to transition from RUNNING to STOPPING and then wait for STOPPED.
     *
     * @param from The expected state
     * @param to The desired state
     * @param waitFor The state to wait for if the transition is successful
     * @return True if the state transitioned
     */
    public boolean transitionAndWait(final State from, final State to, final State waitFor, final Duration maximumWait)
    {
        // If we're in the from state, transition to the to state
        if (state.compareAndSet(from, to))
        {
            // show the state transition,
            trace("$ => $ (wait for $ for up to $)", from, to, waitFor, maximumWait);

            // transmit new state
            stateChanged(to);

            // and then wait for the desired final value
            waitFor(waitFor, maximumWait);
            return true;
        }
        return false;
    }

    /**
     * Transitions to the given state
     *
     * @param to The state to transition to
     * @return The previous state
     */
    public State transitionTo(final State to)
    {
        // Change the state and tell the state watcher that the state has changed,
        final var from = state.getAndSet(to);
        waiter.signal(to);

        // show the state transition,
        trace("$ => $", from, to);

        // transmit new state,
        stateChanged(to);

        // and then return the previous state.
        return from;
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
        trace("Waiting for $ for up to $", state, maximumWait);
        final var wakeState = waiter.waitFor(state, maximumWait);
        trace("State $ reached", state);
        return wakeState == WakeState.COMPLETED;
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

    private void stateChanged(final State state)
    {
        waiter.signal(state);
        if (receiver != null)
        {
            receiver.receive(state);
        }
    }
}
