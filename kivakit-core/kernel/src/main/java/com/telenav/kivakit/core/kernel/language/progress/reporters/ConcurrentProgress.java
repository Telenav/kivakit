////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.progress.reporters;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageProgress;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe subclass of {@link Progress}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageProgress.class)
public class ConcurrentProgress extends Progress
{
    private final AtomicLong at;

    public ConcurrentProgress(final Progress that)
    {
        super(that);
        at = new AtomicLong(that.at());
    }

    protected ConcurrentProgress()
    {
        at = new AtomicLong();
    }

    @Override
    public void at(final long count)
    {
        at.set(count);
    }

    @Override
    public long at()
    {
        return at.get();
    }

    @Override
    protected long increase(final long increase)
    {
        return at.addAndGet(increase);
    }

    @Override
    protected long increment()
    {
        return at.incrementAndGet();
    }

    @Override
    protected @NotNull Progress newInstance()
    {
        return new ConcurrentProgress(this);
    }
}
