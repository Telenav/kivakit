package com.telenav.kivakit.core.kernel.language.threading.conditions;

import com.telenav.kivakit.core.kernel.interfaces.model.Watchable;
import com.telenav.kivakit.core.kernel.language.threading.locks.Lock;
import com.telenav.kivakit.core.kernel.language.threading.status.WakeState;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.mutable.MutableValue;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.concurrent.locks.Condition;
import java.util.function.Predicate;

/**
 * Watches the {@link Watchable} value passed to the constructor.
 * <p>
 * A thread waiting for a particular value calls {@link #waitForValue(Object, Duration, Runnable)} and a thread changing
 * the value calls {@link #valueChanged()}, indicating when the watched value should be re-evaluated by the waiter.
 * </p>
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
public class ValueWatcher<Value>
{
    /** The value to watch */
    private final Watchable<Value> value;

    /** The re-entrant lock */
    final Lock lock = new Lock();

    /** The lock condition to use to signal and await */
    private final Condition condition = lock.newCondition();

    public ValueWatcher(final Watchable<Value> value)
    {
        this.value = value;
    }

    /**
     * Signals that the value being watched has changed and that any thread in {@link #waitForValue(Object, Duration,
     * Runnable)} should wake up and re-evaluate it to see if it is now the desired value.
     */
    public void valueChanged()
    {
        lock.whileLocked(condition::signalAll);
    }

    /**
     * Wait for the given boolean predicate to be true for the given amount of time
     *
     * @see #waitFor(Predicate, Duration, Runnable)
     */
    @UmlRelation(label = "waits until")
    public WakeState waitFor(final Predicate<Value> predicate, final Duration maximumWaitTime)
    {
        return waitFor(predicate, maximumWaitTime, () ->
        {
        });
    }

    /**
     * Wait for the given boolean predicate to be true forever
     *
     * @see #waitFor(Predicate, Duration, Runnable)
     */
    public WakeState waitFor(final Predicate<Value> predicate)
    {
        return waitFor(predicate, Duration.MAXIMUM, () ->
        {
        });
    }

    /**
     * Waits for the given boolean predicate to become true
     *
     * @param maximumWaitTime The maximum amount of time to wait before giving up
     * @param beforeWait Code to execute before waiting. This code might interrupt a thread or perform other tasks to
     * ensure that the wait condition is satisfied.
     */
    public WakeState waitFor(final Predicate<Value> predicate, final Duration maximumWaitTime,
                             final Runnable beforeWait)
    {
        // Initialize the amount of time left to wait
        final MutableValue<Duration> remaining = new MutableValue<>(maximumWaitTime);

        // then loop so long as there is time remaining and the desired value has not been reached
        boolean completed = false;
        while (remaining.get().isSome() && !(completed = predicate.test(value.observe())))
        {
            // run the user's code to wake threads or perform other tasks before waiting
            beforeWait.run();

            // get the time before waiting
            final var now = Time.now();

            // wait for the condition to be signalled
            lock.whileLocked(() -> remaining.get().await(condition));

            // and subtract the wait time from the remaining time
            remaining.update(duration -> duration.subtract(now.elapsedSince()));
        }

        return completed ? WakeState.COMPLETED : WakeState.TIMED_OUT;
    }

    /**
     * Wait for the given value to be reached by checking the {@link Watchable} passed to the constructor
     *
     * @param desired The value for which to wait
     * @param maximumWaitTime The maximum amount of time to wait before giving up
     * @param beforeWait Code to execute before waiting. This code might interrupt a thread or perform other tasks to
     * ensure that the wait condition is satisfied.
     */
    public WakeState waitForValue(final Value desired, final Duration maximumWaitTime, final Runnable beforeWait)
    {
        // Wait for the value we observe to be the desired value
        return waitFor(value -> value.equals(desired), maximumWaitTime, beforeWait);
    }

    /**
     * Wait forever for a value, running the provided code before each wait cycle.
     *
     * @see #waitForValue(Object, Duration, Runnable)
     */
    public WakeState waitForValue(final Value value)
    {
        return waitForValue(value, Duration.MAXIMUM);
    }

    /**
     * Wait for a value for the given amount of time
     *
     * @see #waitForValue(Object, Duration, Runnable)
     */
    public WakeState waitForValue(final Value value, final Duration maximumWaitTime)
    {
        return waitForValue(value, maximumWaitTime, () ->
        {
        });
    }
}
