package com.telenav.kivakit.core.code;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * {@link UncheckedCode} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface UncheckedVoidCode extends RepeaterMixin
{
    /**
     * Removes exception checking from the given code
     *
     * @param code The code
     * @return The unchecked code
     */
    static UncheckedVoidCode unchecked(UncheckedVoidCode code)
    {
        return code;
    }

    /**
     * Loops forever, running this code
     *
     * @param listener Listener to broadcast warnings when exceptions are thrown
     * @param pause Time to delay between executions
     */
    @SuppressWarnings("InfiniteLoopStatement")
    default void loop(Listener listener, Duration pause)
    {
        while (true)
        {
            try
            {
                run();
            }
            catch (Exception e)
            {
                listener.warning(e, "Continuing after code threw exception");
            }

            pause.sleep();
        }
    }

    /**
     * Runs this code
     *
     * @throws Exception The exception that might be thrown by the code
     */
    void run() throws Exception;
}
