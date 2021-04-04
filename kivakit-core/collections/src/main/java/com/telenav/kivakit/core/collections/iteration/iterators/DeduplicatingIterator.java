////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.iteration.iterators;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements an iterator which can deduplicate the objects returned by the wrapped iterator. Each element
 * returned by the wrapped iterator is kept in a {@link Set} and only values that have not been seen before are produced
 * as part of the sequence for the caller. The iterated values must correctly implement the {@link #hashCode()} / {@link
 * #equals(Object)} contract.
 *
 * @author Junwei
 * @author jonathanl (shibo)
 * @version 1.0.0 2012-8-27
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class DeduplicatingIterator<Element> extends BaseIterator<Element>
{
    private final Iterator<Element> iterator;

    private final Set<Element> existing = new HashSet<>();

    public DeduplicatingIterator(final Iterator<Element> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    protected Element onNext()
    {
        if (iterator.hasNext())
        {
            final var next = iterator.next();
            if (!existing.contains(next))
            {
                existing.add(next);
                return next;
            }
            else
            {
                return onNext();
            }
        }
        else
        {
            return null;
        }
    }
}
