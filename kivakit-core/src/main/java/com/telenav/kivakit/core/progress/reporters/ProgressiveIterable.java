package com.telenav.kivakit.core.progress.reporters;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.iteration.BaseIterable;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.interfaces.collection.NextIterator;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A wrapper that adds {@link ProgressReporter} reporting to an iterable
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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
