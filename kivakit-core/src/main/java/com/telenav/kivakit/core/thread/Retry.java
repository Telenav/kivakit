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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.language.reflection.Type.type;
import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;

/**
 * Retry running a {@link Runnable}
 *
 * @author matthieun
 * @author jonathanl
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramThread.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Retry extends BaseRepeater
{
    public static final int DEFAULT_RETRIES = 5;

    /**
     * Retries the given code the given number of times with the given delay between attempts.
     *
     * @param code The code to run
     * @param times The number of times to run it
     * @param delay The delay between attempts
     * @param beforeRetry Any code to run before retrying
     */
    public static <T> UncheckedCode<T> retry(Listener listener,
                                             UncheckedCode<T> code,
                                             int times,
                                             Duration delay,
                                             Runnable... beforeRetry)
    {
        return () ->
        {
            var retry = new Retry(listener, times, delay, Exception.class);
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
        this(listener, DEFAULT_RETRIES, ZERO_DURATION, Throwable.class);
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
            return fail(e, "Execution with retry of " + e.getMessage() + " failed ");
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
            return fail("Cannot have a negative " + numberOfRetries + " number of retries.");
        }
        if (totalRetries < 0)
        {
            return fail("Cannot have a negative " + totalRetries + " total number of retries.");
        }
        if (numberOfRetries > DEFAULT_RETRIES)
        {
            // Protect against excessive retry.
            numberOfRetries = DEFAULT_RETRIES;
        }
        if (totalRetries > DEFAULT_RETRIES)
        {
            // Protect against excessive retry.
            totalRetries = DEFAULT_RETRIES;
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
                Type<? extends Exception> errorType = type(e);
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
                    return fail(e, "Unable to run");
                }
            }
        }
        else
        {
            // All the retries have been exhausted, propagate the exception
            return fail("Failed run " + runnable + " after " + totalRetries + " retries.");
        }

        return fail("Unable to run");
    }
}
