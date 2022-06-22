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

import com.mastfrog.shutdown.hooks.ShutdownHookRegistry;
import com.mastfrog.shutdown.hooks.ShutdownHooks;
import com.telenav.kivakit.core.lexakai.DiagramLanguage;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import java.util.concurrent.ExecutorService;



/**
 * Adds order-of-execution to {@link Runtime#addShutdownHook(Thread)}. Hooks can request that they be run {@link
 * Order#FIRST} or {@link Order#LAST}. The order of two hooks both requesting to be first or last is not defined. It is
 * only guaranteed that hooks asking to be run last will be run after hooks requesting to be run first.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguage.class)
public class ShutdownHook
{
    private static final int MILLISECONDS_TO_WAIT = 1200000;
    private static final ShutdownHooks REGISTRY
            = ShutdownHookRegistry.get(MILLISECONDS_TO_WAIT);

    public static void register(Order order, Runnable code)
    {
        // Pending:  ShutdownHooks has a much richer API for adding
        // things, with specific handling for AutoCloseables, Timers, and much more,
        // and support for adding things only held by a weak reference, so
        // shutdown tasks do not become a source of memory leaks.
        //
        // We can decide as we go what other things are usefully exposed here.
        switch(order) {
            case FIRST :
                REGISTRY.add(code);
                break;
            case LAST :
                REGISTRY.addLast(code);
        }
    }
    
    /**
     * Register an ExecutorService to wait for on exit; all executors
     * share a timeout.  ExecutorServices will be weakly referenced.
     * 
     * @param order The order to add in
     * @param threadPool The thread pool to await shutdown on
     */
    public static void register(Order order, ExecutorService threadPool)
    {
        switch(order) {
            case FIRST :
                REGISTRY.add(threadPool);
                break;
            case LAST :
                REGISTRY.addLast(threadPool);
        }
    }
    
    /**
     * Explicitly invoke shutdown logic - this is useful from a test harness
     * or isolating classloader before closing it.  Once run, the shutdown
     * hook tasks are cleared and any registered JVM shutdown hook deregistered.
     */
    public static void shutdown() {
        REGISTRY.shutdown();
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

        /** The hook should be run after hooks that are marked as FIRST */
        LAST
    }
}
