package com.telenav.kivakit.core.kernel.language.vm;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageJavaVirtualMachine;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Adds order-of-execution to {@link Runtime#addShutdownHook(Thread)}. Hooks can request that they be run {@link
 * Order#FIRST} or {@link Order#LAST}. The order of two hooks both requesting to be first or last is not defined. It is
 * only guaranteed that hooks asking to be run last will be run after hooks requesting to be run first.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageJavaVirtualMachine.class)
public class KivaKitShutdownHook
{
    private static final LinkedList<KivaKitShutdownHook> queue = new LinkedList<>();

    static
    {
        final var shutdown = new Thread(() ->
        {
            final List<KivaKitShutdownHook> copy;
            synchronized (queue)
            {
                copy = new ArrayList<>(queue);
            }
            for (final var hook : copy)
            {
                hook.execute();
            }
        });
        shutdown.setName("KivaKit Shutdown");
        Runtime.getRuntime().addShutdownHook(shutdown);
    }

    public enum Order
    {
        /** The hook should be run before hooks that are marked as LAST */
        FIRST,

        /** The hook should be run after hooks that are marked as FIRST */
        LAST
    }

    private final Runnable code;

    public KivaKitShutdownHook(final Order order, final Runnable code)
    {
        synchronized (queue)
        {
            switch (order)
            {
                case FIRST:
                    queue.addFirst(this);
                    break;

                case LAST:
                    queue.add(this);
                    break;
            }
        }

        this.code = code;
    }

    private void execute()
    {
        code.run();
    }
}
