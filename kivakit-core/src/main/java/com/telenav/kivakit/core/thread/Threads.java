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
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.MutableCount;
import com.telenav.kivakit.core.vm.JavaVirtualMachine;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Methods for working with threads and {@link ExecutorService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Threads
{
    /**
     * Returns all threads in the virtual machine
     */
    public static Iterable<Thread> all()
    {
        // Get the root thread group
        var rootGroup = rootGroup();

        // Initially expect the larger of 8 or the number of active threads
        var expected = Math.max(8, rootGroup.activeCount());

        // Allocate initial array for threads
        var enumerated = new Thread[expected];

        // Loop until we get all the threads at once
        int count;
        while ((count = rootGroup.enumerate(enumerated, true)) == enumerated.length)
        {
            expected *= 2;
            enumerated = new Thread[expected];
        }

        // Return the array as a list
        return new ArrayList<>(Arrays.asList(enumerated).subList(0, count));
    }

    /**
     * Awaits termination of the given executor
     */
    public static void awaitTermination(ExecutorService executor)
    {
        UncheckedCode.unchecked(() -> executor.awaitTermination(Long.MAX_VALUE, MILLISECONDS)).orNull();
    }

    /**
     * Returns the root thread group
     */
    public static ThreadGroup rootGroup()
    {
        ThreadGroup root = null;
        for (var current = Thread.currentThread().getThreadGroup(); current != null; current = current.getParent())
        {
            root = current;
        }
        return root;
    }

    /**
     * Tells the given executor to shutdown, then waits for it to do so
     */
    public static void shutdownAndAwaitTermination(ExecutorService executor)
    {
        executor.shutdown();
        if (!executor.isShutdown() && !executor.isTerminated())
        {
            awaitTermination(executor);
        }
    }

    /**
     * Returns an {@link ExecutorService} with the given name and thread count
     */
    public static ExecutorService threadPool(String name, Count threads)
    {
        var identifier = new MutableCount(1);
        return Executors.newFixedThreadPool(threads.asInt(), runnable ->
                new Thread(runnable, "KivaKit-" + name + "-" + identifier.increment()));
    }

    /**
     * Returns a thread pool with the given name and one thread per processor
     */
    public static ExecutorService threadPool(String name)
    {
        return threadPool(name, JavaVirtualMachine.javaVirtualMachine().processors());
    }
}
