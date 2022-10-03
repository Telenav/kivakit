package com.telenav.kivakit.core.code;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * {@link UncheckedCode} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@ApiQuality(stability = API_STABLE_DEFAULT_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
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
