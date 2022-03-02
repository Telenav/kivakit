package com.telenav.kivakit.core.code;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

/**
 * {@link UncheckedCode} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface UncheckedVoidCode
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
