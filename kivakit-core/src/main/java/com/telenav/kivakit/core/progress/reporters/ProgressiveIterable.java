package com.telenav.kivakit.core.progress.reporters;

import com.telenav.kivakit.core.collections.iteration.BaseIterable;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.interfaces.collection.NextValue;

import java.util.Iterator;

/**
 * A wrapper that adds {@link ProgressReporter} reporting to an iterable
 *
 * @author jonathanl (shibo)
 */
public class ProgressiveIterable<T> extends BaseIterable<T>
{
    private final Iterator<T> iterator;

    private final ProgressReporter reporter;

    public ProgressiveIterable(Iterable<T> iterable, ProgressReporter reporter)
    {
        this.iterator = iterable.iterator();
        this.reporter = reporter;
    }

    @Override
    protected NextValue<T> newNext()
    {
        return () ->
        {
            if (iterator.hasNext())
            {
                reporter.next();
                return iterator.next();
            }

            return null;
        };
    }
}
