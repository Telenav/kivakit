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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.code.UncheckedCode.unchecked;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Retry running a {@link Runnable}
 *
 * @author matthieun
 * @author jonathanl
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramThread.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Retry extends BaseRepeater
{
    /**
     * Retries the given code the given number of times with the given delay between attempts.
     *
     * @param times The number of times to run it
     * @param delay The delay between attempts
     * @param code The code to run
     * @param beforeRetry Any code to run before retrying
     */
    public static <T> UncheckedCode<T> retry(Listener listener,
                                             Count times,
                                             Duration delay,
                                             UncheckedCode<T> code,
                                             Runnable... beforeRetry)
    {
        return () -> new Retry(listener, times, delay, Exception.class).run(code, beforeRetry);
    }

    /**
     * Retries the given code the given number of times with the given delay between attempts.
     *
     * @param code The code to run
     * @param times The number of times to run it
     * @param delay The delay between attempts
     * @param beforeRetry Any code to run before retrying
     */
    public static void retry(Listener listener,
                             Count times,
                             Duration delay,
                             UncheckedVoidCode code,
                             Runnable... beforeRetry)
    {
        var unchecked = unchecked(() ->
        {
            code.run();
            return null;
        });

        new Retry(listener, times, delay, Exception.class).run(unchecked, beforeRetry);
    }

    private final Count numberOfRetries;

    private final Duration retryWaitTime;

    final Class<? extends Throwable> exceptionType;

    /**
     * A retry tool to run a {@link Runnable} as long as there is one type of exception
     *
     * @param listener Listener for the broadcasts
     * @param numberOfRetries The number of failures allowed
     * @param retryWaitDuration The duration to wait between retries
     * @param exceptionType The type of exception to be caught. It needs to be a type that extends {@link Throwable}
     */
    public Retry(Listener listener,
                 Count numberOfRetries,
                 Duration retryWaitDuration,
                 Class<? extends Throwable> exceptionType)
    {
        this.numberOfRetries = numberOfRetries;
        retryWaitTime = retryWaitDuration;
        this.exceptionType = exceptionType;
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
            return runWithRetries(numberOfRetries, runnable, stepsBeforeRetry);
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
     * @param numberOfRetries The number of retries in this step
     * @param runnable The runnable to be tried
     * @param beforeRetry This is an optional list of steps executed in case the attempt fails
     */
    private <T> T runWithRetries(Count numberOfRetries,
                                 UncheckedCode<T> runnable,
                                 Runnable... beforeRetry)
    {
        try
        {
            return runnable.run();
        }
        catch (Exception e)
        {
            // If we have more retries left,
            if (numberOfRetries.isNonZero())
            {
                // warn that we are retrying,
                warning(e, "Retrying operation: $", runnable.getClass().getSimpleName());

                // wait a moment,
                retryWaitTime.sleep();

                // execute steps before retrying (to hopefully correct the problem)
                list(beforeRetry).forEach(Runnable::run);

                // then recurse with one less retry.
                runWithRetries(numberOfRetries.decremented(), runnable, beforeRetry);
            }

            // No more retries are left!
            return fail(e, "Out of retries: $", runnable.getClass().getSimpleName());
        }
    }
}
