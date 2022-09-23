package com.telenav.kivakit.core.code;

import com.telenav.kivakit.annotations.code.ApiStability;
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.time.Duration;

import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * {@link UncheckedCode} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@ApiQuality(stability = ApiStability.STABLE,
            testing = NONE,
            documentation = SUFFICIENT)
public interface UncheckedVoidCode extends RepeaterMixin
{
    static UncheckedVoidCode of(UncheckedVoidCode code)
    {
        return code;
    }

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

    void run() throws Exception;
}
