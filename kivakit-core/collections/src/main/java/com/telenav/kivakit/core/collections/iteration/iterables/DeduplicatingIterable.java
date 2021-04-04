////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.iteration.iterables;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.iteration.iterators.DeduplicatingIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * An {@link Iterable} that wraps another iterable, removing duplicate values. The iterated values must correctly
 * implement the {@link #hashCode()} / {@link #equals(Object)} contract.
 *
 * @author Junwei
 * @version 1.0.0 2013-7-1
 * @see DeduplicatingIterator
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class DeduplicatingIterable<Element> implements Iterable<Element>
{
    private final Iterable<Element> iterable;

    public DeduplicatingIterable(final Iterable<Element> iterable)
    {
        this.iterable = iterable;
    }

    @Override
    public @NotNull Iterator<Element> iterator()
    {
        return new DeduplicatingIterator<>(iterable.iterator());
    }
}
