package com.telenav.kivakit.core.progress.reporters;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.iteration.BaseIterable;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.interfaces.collection.NextIterator;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A wrapper that adds {@link ProgressReporter} reporting to an iterable
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ProgressiveIterable<Value> extends BaseIterable<Value>
{
    /** The iterator to report on */
    private final Iterator<Value> iterator;

    /** The reporter to notify */
    private final ProgressReporter reporter;

    /**
     * Creates an iterable that reports progress
     *
     * @param iterable The iterable to access values
     * @param reporter The reporter to notify
     */
    public ProgressiveIterable(Iterable<Value> iterable, ProgressReporter reporter)
    {
        this.iterator = iterable.iterator();
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NextIterator<Value> newNextIterator()
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
