////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading;

import com.telenav.kivakit.core.kernel.interfaces.code.Code;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.mutable.MutableIndex;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Methods for working with threads and {@link ExecutorService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
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
        Code.of(() -> executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS)).orNull();
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
