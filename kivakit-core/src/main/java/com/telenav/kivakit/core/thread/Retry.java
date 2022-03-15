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

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.lexakai.DiagramThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Retry running a {@link Runnable}
 *
 * @author matthieun
 * @author jonathanl
 */
@UmlClassDiagram(diagram = DiagramThread.class)
public class Retry extends BaseRepeater
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static final int MAXIMUM_NUMBER_RETRIES = 5;

    public static <T> UncheckedCode<T> retry(UncheckedCode<T> code,
                                             int times,
                                             Duration delay,
                                             Runnable... beforeRetry)
    {
        return () ->
        {
            var retry = new Retry(LOGGER, times, delay, Exception.class);
            return retry.run(code, beforeRetry);
        };
    }

    private final String[] exceptionMessageExclusion;

    private final int numberOfRetries;

    private final Duration retryWaitTime;

    final Class<? extends Throwable> exceptionType;

    /**
     * A basic {@link Retry} that does not wait before retrying and catches all {@link Throwable} errors.
     */
    public Retry(Listener listener)
    {
        this(listener, MAXIMUM_NUMBER_RETRIES, Duration.NONE, Throwable.class);
    }

    /**
     * A retry tool to run a {@link Runnable} as long as there is one type of exception
     *
     * @param listener Listener for the broadcasts
     * @param numberOfRetries The number of failures allowed
     * @param retryWaitDuration The duration to wait between retries
     * @param exceptionType The type of exception to be caught. It needs to be a type that extends {@link Throwable}
     * @param exceptionMessageExclusion An arbitrary number of Strings. If the exception caught has a message that
     * contains one of the Strings contained in this set, the exception will be thrown and no retry will be made.
     */
    public Retry(Listener listener,
                 int numberOfRetries,
                 Duration retryWaitDuration,
                 Class<? extends Throwable> exceptionType,
                 String... exceptionMessageExclusion)
    {
        this.numberOfRetries = numberOfRetries;
        retryWaitTime = retryWaitDuration;
        this.exceptionType = exceptionType;
        this.exceptionMessageExclusion = exceptionMessageExclusion;
        addListener(listener);
    }

    /**
     * Run a {@link Runnable} and retry as long as the exception type defined is thrown
     *
     * @param runnable The runnable to be tried
     * @param stepsBeforeRetry This is an optional list of steps executed in case the try fails, and right before each
     * retry
     */
    public <T> T run(UncheckedCode<T> runnable, Runnable... stepsBeforeRetry)
    {
        try
        {
            return runWithRetries(runnable, numberOfRetries, numberOfRetries, stepsBeforeRetry);
        }
        catch (Exception e)
        {
            return Ensure.fail(e, "Execution with retry of " + e.getMessage() + " failed ");
        }
    }

    /**
     * Run a runnable with a specified number of retries to cope with sporadic exceptions, like blob server busy. This
     * function is recursive.
     *
     * @param runnable The runnable to be tried
     * @param numberOfRetries The number of retries in this step. Any number of retries larger than 5 will be capped at
     * 5.
     * @param totalRetries The total number of retries from the beginning, for reporting purposes only.
     * @param stepsBeforeRetry This is an optional list of steps executed in case the try fails, and right before each
     * retry
     */
    private <T> T runWithRetries(UncheckedCode<T> runnable, int numberOfRetries, int totalRetries,
                                 Runnable... stepsBeforeRetry)
    {
        if (numberOfRetries < 0)
        {
            return Ensure.fail("Cannot have a negative " + numberOfRetries + " number of retries.");
        }
        if (totalRetries < 0)
        {
            return Ensure.fail("Cannot have a negative " + totalRetries + " total number of retries.");
        }
        if (numberOfRetries > MAXIMUM_NUMBER_RETRIES)
        {
            // Protect against excessive retry.
            numberOfRetries = MAXIMUM_NUMBER_RETRIES;
        }
        if (totalRetries > MAXIMUM_NUMBER_RETRIES)
        {
            // Protect against excessive retry.
            totalRetries = MAXIMUM_NUMBER_RETRIES;
        }
        if (numberOfRetries > 0)
        {
            try
            {
                if (totalRetries - numberOfRetries > 0)
                {
                    /* We want to run the steps before retry if we are not in the first try */
                    for (var step : stepsBeforeRetry)
                    {
                        step.run();
                    }
                }
                return runnable.run();
            }
            catch (Exception e)
            {
                Type<? extends Exception> errorType = Type.of(e);
                var shouldRetry = false;
                if (errorType.isDescendantOf(exceptionType))
                {
                    shouldRetry = true;
                    for (var messageSnippet : exceptionMessageExclusion)
                    {
                        if (e.getMessage() != null && e.getMessage().contains(messageSnippet))
                        {
                            shouldRetry = false;
                        }
                    }
                }
                if (shouldRetry)
                {
                    warning(e, "Failed ${debug} for ${debug}",
                            (totalRetries - numberOfRetries == 0 ? "first try"
                                    : "retry " + (totalRetries - numberOfRetries)),
                            runnable);

                    // Exhaust one retry and call the method again after a short while
                    if (numberOfRetries - 1 > 0)
                    {
                        // Sleep only if there are still some unused retries left
                        retryWaitTime.sleep();
                    }
                    runWithRetries(runnable, numberOfRetries - 1, totalRetries);
                }
                else
                {
                    return Ensure.fail(e, "Unable to run");
                }
            }
        }
        else
        {
            // All the retries have been exhausted, propagate the exception
            return Ensure.fail("Failed run " + runnable + " after " + totalRetries + " retries.");
        }

        return Ensure.fail("Unable to run");
    }
}
