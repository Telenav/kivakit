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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLanguage;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.loggers.ConsoleLogger;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.thread.KivaKitThread.State.EXITED;
import static com.telenav.kivakit.core.vm.ShutdownHook.Order.MIDDLE;
import static com.telenav.kivakit.interfaces.time.WakeState.TIMED_OUT;

/**
 * Adds <i>ordered</i> execution of shutdown hooks to the functionality provided by
 * {@link Runtime#addShutdownHook(Thread)}. When the virtual machine shuts down, the hooks registered with
 * {@link ShutdownHook} will be called sequentially according to the ordering provided when the hooks were registered
 * with {@link #register(String, Order, Duration, Runnable)}.
 *
 * <p>
 * Hooks can request that they be run {@link Order#FIRST}, {@link Order#MIDDLE} or {@link Order#LAST}. The order of
 * execution hooks assigned the <i>same</i> {@link Order} is not defined. Otherwise hooks will be executed in sorted
 * order, from {@link Order#FIRST} to {@link Order#LAST}.
 * </p>
 *
 * <i><b>NOTE:</b></i>
 * <p>
 * This class simply allows pre-registered shutdown hooks to be run in a defined order during VM shutdown. For more
 * complex shutdown hook requirements, see <a
 * href="https://mvnrepository.com/artifact/com.mastfrog/shutdown-hooks">com.mastfrog:shutdown-hooks</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguage.class)
@ApiQuality(stability = STABLE_STATIC_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class ShutdownHook implements Comparable<ShutdownHook>
{
    /** We use a console logger here because it is never involved in shutdown processes */
    private static final Logger LOGGER = new ConsoleLogger();

    /** A priority queue of shutdown hooks sorted in an assigned {@link Order} */
    private static PriorityQueue<ShutdownHook> hooks;

    /** True when shutdown is in process */
    private static boolean shuttingDown = false;

    static
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            synchronized (hooks())
            {
                // Start shutting down,
                shuttingDown = true;

                // and while there are more hooks to execute,
                while (!hooks().isEmpty())
                {
                    // take a hook out of the queue,
                    var hook = hooks().remove();

                    // and run it to completion or timeout, whichever comes first.
                    var thread = new KivaKitThread("ShutdownHook-" + hook.name, hook.code);
                    if (thread.start())
                    {
                        if (thread.waitFor(EXITED, hook.maximumWait) == TIMED_OUT)
                        {
                            LOGGER.problem("Timed out waiting for shutdown hook to complete: $");
                        }
                    }
                    else
                    {
                        LOGGER.problem("Unable to start shutdown hook: $");
                    }
                }
            }
        }, "KivaKit-Shutdown"));
    }

    public static void register(String name, Order order, Runnable code)
    {
        register(name, order, Duration.minutes(1), code);
    }

    public static void register(String name, Duration maximumWait, Runnable code)
    {
        register(name, MIDDLE, maximumWait, code);
    }

    public static void register(String name, Runnable code)
    {
        register(name, MIDDLE, code);
    }

    /**
     * Registers the given shutdown hook code to run in the given serial order.
     *
     * @param name The name of the shutdown hook
     * @param order The {@link Order} in which the hook should run. Hooks with the same {@link Order} execute in an
     * undefined order.
     * @param maximumWait The maximum time to wait for the hook's code to complete
     * @param code The shutdown hook code to run when the VM shuts down.
     */
    public static void register(String name, Order order, Duration maximumWait, Runnable code)
    {
        synchronized (hooks())
        {
            // Do not allow registration of further hooks during shutdown
            if (shuttingDown)
            {
                throw new IllegalStateException("Cannot register ShutdownHook once shutdown has begun");
            }

            // Add a shutdown hook for the given code that will execute in the given order.
            hooks().add(new ShutdownHook(order, code, maximumWait, name));
        }
    }

    /**
     * The order that a hook should be run in, either among the set of first hooks, or among the set of last hooks. The
     * only guarantee is that a hook that is FIRST will run before any hook that is LAST
     */
    @LexakaiJavadoc(complete = true)
    public enum Order
    {
        /** The hook should be run before hooks that are marked as LAST */
        FIRST,

        /** The hook should be run after hooks that are marked as FIRST, but before those marked as LAST */
        MIDDLE,

        /** The hook should be run after hooks that are marked as FIRST */
        LAST
    }

    /** The code to run */
    private final Runnable code;

    /** The order in which this code should be run */
    private final Order order;

    /** The maximum time to wait for each shutdown hook to finish */
    private final Duration maximumWait;

    /** The name of this shutdown hook */
    private final String name;

    private ShutdownHook(Order order, Runnable code, Duration maximumWait, String name)
    {
        this.code = code;
        this.order = order;
        this.maximumWait = maximumWait;
        this.name = name;
    }

    @Override
    public int compareTo(@NotNull ShutdownHook that)
    {
        return order.compareTo(that.order);
    }

    @Override
    public String toString()
    {
        return Strings.format("[${class} name = $, order = $]", getClass(), name, order);
    }

    private static synchronized PriorityQueue<ShutdownHook> hooks()
    {
        if (hooks == null)
        {
            hooks = new PriorityQueue<>();
        }
        return hooks;
    }
}
