package com.telenav.kivakit.core.kernel.language.threading.locks;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A lock subclass that adds convenient features to {@link ReentrantLock}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
public class ReadWriteLock extends ReentrantReadWriteLock
{
    public ReadWriteLock()
    {
        super(true);
    }

    /**
     * Runs the provided code inside a lock / unlock pair, allowing the codes to have a return value.
     */
    public <T> T read(final Source<T> code)
    {
        readLock().lock();
        try
        {
            return code.get();
        }
        finally
        {
            readLock().unlock();
        }
    }

    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public void read(final Runnable code)
    {
        readLock().lock();
        try
        {
            code.run();
        }
        finally
        {
            readLock().unlock();
        }
    }

    /**
     * Runs the provided code inside a lock / unlock pair, allowing the code to have a return value.
     */
    public <T> T write(final Source<T> code)
    {
        writeLock().lock();
        try
        {
            return code.get();
        }
        finally
        {
            writeLock().unlock();
        }
    }

    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public void write(final Runnable code)
    {
        writeLock().lock();
        try
        {
            code.run();
        }
        finally
        {
            writeLock().unlock();
        }
    }
}
