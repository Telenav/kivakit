////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.threading;

import com.telenav.kivakit.kernel.interfaces.code.CheckedCode;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.mutable.MutableIndex;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Methods for working with threads and {@link ExecutorService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
@LexakaiJavadoc(complete = true)
public class Threads
{
    public static Iterable<Thread> all()
    {
        // Get the root thread group
        final var rootGroup = rootGroup();

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

    public static void await(final ExecutorService executor)
    {
        CheckedCode.of(() -> executor.awaitTermination(Long.MAX_VALUE, MILLISECONDS)).orNull();
    }

    public static ThreadGroup rootGroup()
    {
        ThreadGroup root = null;
        for (var current = Thread.currentThread().getThreadGroup(); current != null; current = current.getParent())
        {
            root = current;
        }
        return root;
    }

    public static void shutdownAndAwait(final ExecutorService executor)
    {
        executor.shutdown();
        await(executor);
    }

    public static ExecutorService threadPool(final String name, final Count threads)
    {
        final var identifier = new MutableIndex(1);
        return Executors.newFixedThreadPool(threads.asInt(), runnable ->
                new Thread(runnable, "KivaKit-" + name + "-" + identifier.increment()));
    }

    public static ExecutorService threadPool(final String name)
    {
        return threadPool(name, JavaVirtualMachine.local().processors());
    }
}
