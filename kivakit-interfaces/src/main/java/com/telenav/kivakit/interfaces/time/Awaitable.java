package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import java.util.concurrent.TimeUnit;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to a JDK method that takes a duration and time unit. This allows integration with
 * {@link LengthOfTime#await(Awaitable)}. The result (using the Duration subclass of {@link LengthOfTime}) looks like
 * this:
 *
 * <pre>
 * Condition condition = [...];
 *
 *     [...]
 *
 * minutes(3).await(condition);</pre>
 *
 * @author jonathanl (shibo)
 * @see LengthOfTime
 */
@FunctionalInterface
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface Awaitable
{
    /**
     * A method that accepts a wait time in a given unit
     *
     * @param wait The time to wait
     * @param unit The time unit
     * @return True if the awaited condition occurred, false if it timed out.
     */
    boolean await(long wait, TimeUnit unit) throws InterruptedException;
}
