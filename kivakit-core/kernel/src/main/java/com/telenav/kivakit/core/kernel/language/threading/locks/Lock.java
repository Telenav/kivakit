package com.telenav.kivakit.core.kernel.language.threading.locks;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.locks.ReentrantLock;

/**
 * A lock subclass that adds convenient features to {@link ReentrantLock}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
public class Lock extends ReentrantLock
{
    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public void whileLocked(final Runnable code)
    {
        lock();
        try
        {
            code.run();
        }
        finally
        {
            unlock();
        }
    }
}
