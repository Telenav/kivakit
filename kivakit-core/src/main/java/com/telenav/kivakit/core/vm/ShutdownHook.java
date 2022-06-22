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
package com.telenav.kivakit.core.vm;

import com.mastfrog.function.throwing.ThrowingRunnable;
import com.mastfrog.shutdown.hooks.ShutdownHookRegistry;
import com.mastfrog.shutdown.hooks.ShutdownHooks;
import com.telenav.kivakit.core.lexakai.DiagramLanguage;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Adds order-of-execution to {@link Runtime#addShutdownHook(Thread)}. Hooks can
 * request that they be run {@link
 * Order#FIRST} or {@link Order#LAST}. The order of two hooks both requesting to
 * be first or last is not defined. It is only guaranteed that hooks asking to
 * be run last will be run after hooks requesting to be run first.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguage.class)
public class ShutdownHook
{
    // Pending - what do we actually think is reasonable here?
    private static final Duration TIME_TO_WAIT = Duration.ONE_HOUR.times(2);

    private static final ShutdownHooks REGISTRY
            = ShutdownHookRegistry.get(TIME_TO_WAIT.asJavaDuration());

    /**
     * Register a Runnable to run on VM shutdown or when <code>shutdown()</code>
     * is called.
     *
     * @param order The order to add in
     * @param code The runnable
     */
    public static void register(Order order, Runnable code)
    {
        switch (order)
        {
            case FIRST:
                REGISTRY.add(code);
                break;
            case LAST:
                REGISTRY.addLast(code);
        }
    }

    /**
     * Register a Runnable to run on VM shutdown or when <code>shutdown()</code>
     * is called, which will be <i>weakly referenced</i> such that it may become
     * unreferenced and be garbage collected. Note that lambdas passed here will
     * be <u>instantly </u> garbage collected - use this for an object that
     * lives a long time, that needs some shutdown cleanup.
     *
     * @param order The order to add in
     * @param code The runnable
     */
    public static void registerWeak(Order order, Runnable code)
    {
        switch (order)
        {
            case FIRST:
                REGISTRY.addWeak(code);
                break;
            case LAST:
                REGISTRY.addLastWeak(code);
        }
    }

    /**
     * Register an ExecutorService to shut down and wait for on exit; all
     * executors share a timeout. ExecutorServices will be weakly referenced.
     *
     * @param order The order to add in
     * @param threadPool The thread pool to await shutdown on
     */
    public static void register(Order order, ExecutorService threadPool)
    {
        switch (order)
        {
            case FIRST:
                REGISTRY.add(threadPool);
                break;
            case LAST:
                REGISTRY.addLast(threadPool);
        }
    }

    /**
     * Register an AutoCloseable to close on exit. AutoCloseable will be weakly
     * referenced.
     *
     * @param order The order to add in
     * @param resource The resource to close
     */
    public static void registerResource(Order order, AutoCloseable resource)
    {
        // Note this method is named differently as the compiler cannot
        // differentiate if you are passing a runnable or ThrowingRunnable
        // or AutoCloseable, in the case of a lambda, and we do not want to
        // force people to cast, e.g. add((Runnable) () -> {...})
        switch (order)
        {
            case FIRST:
                REGISTRY.addResource(resource);
                break;
            case LAST:
                REGISTRY.addResourceLast(resource);
        }
    }

    /**
     * Register a Timer to shut down on exit; all executors share a timeout. The
     * timer will be weakly referenced.
     *
     * @param order The order to add in
     * @param timer The timer to stop
     */
    public static void register(Order order, Timer timer)
    {
        switch (order)
        {
            case FIRST:
                REGISTRY.add(timer);
                break;
            case LAST:
                REGISTRY.addLast(timer);
        }
    }

    /**
     * Register a <code>Callable&lt;&gt;</code> to run on exit.
     *
     * @param order The order to add in
     * @param toCall The callable
     */
    public static void register(Order order, Callable<?> toCall)
    {
        switch (order)
        {
            case FIRST:
                REGISTRY.add(toCall);
                break;
            case LAST:
                REGISTRY.addLast(toCall);
        }
    }

    /**
     * Register a <code>ThrowingRunnable</code> to run on exit. If it throws an
     * exception, it will <b>not</b> prevent other hooks from running. All hooks
     * are guaranteed to be invoked, regardless of the outcome of any others.
     *
     * @param order The order to add in
     * @param toCall The callable
     */
    public static void registerThrowing(Order order, ThrowingRunnable toCall)
    {
        // Note this method is named differently as the compiler cannot
        // differentiate if you are passing a runnable or ThrowingRunnable
        // in the case of a lambda, and we do not want to force people to
        // cast, e.g. add((Runnable) () -> {...})
        switch (order)
        {
            case FIRST:
                REGISTRY.addThrowing(toCall);
                break;
            case LAST:
                REGISTRY.addLastThrowing(toCall);
        }
    }

    /**
     * Explicitly invoke shutdown logic - this is useful from a test harness or
     * isolating classloader before closing it. Once run, the shutdown hook
     * tasks are cleared and any registered JVM shutdown hook deregistered.
     * <p>
     * Shutdown hook tasks may be added <i>while</i> the shutdown hooks are
     * running, and they are guaranteed to be run in that round; tasks add
     * <i>after</i> the shutdown run has completed may cause the shutdown hooks
     * to become re-registered as a VM shutdown hook.
     * </p>
     */
    public static void shutdown()
    {
        REGISTRY.shutdown();
    }

    /**
     * The order that a hook should be run in, either among the set of first
     * hooks, or among the set of last hooks. The only guarantee is that a hook
     * that is FIRST will run before any hook that is LAST
     */
    @LexakaiJavadoc(complete = true)
    public enum Order
    {
        /**
         * The hook should be run before hooks that are marked as LAST
         */
        FIRST,

        /**
         * The hook should be run after hooks that are marked as FIRST
         */
        LAST
    }
}
