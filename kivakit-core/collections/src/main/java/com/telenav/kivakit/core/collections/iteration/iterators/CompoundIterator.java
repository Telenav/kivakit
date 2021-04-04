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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterator} that wraps and iterates through a sequence of {@link Iterator}s in the order they are added,
 * either by constructor or with {@link #add(Iterator)}.
 *
 * @author jonathanl (shibo)
 * @see BaseIterator
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class CompoundIterator<Element> extends BaseIterator<Element>
{
    final List<Iterator<Element>> iterators = new ArrayList<>();

    private int index;

    public CompoundIterator()
    {
    }

    @SafeVarargs
    public CompoundIterator(final Iterator<Element> iterator, final Iterator<Element>... iterators)
    {
        add(iterator);
        Collections.addAll(this.iterators, iterators);
    }

    public void add(final Iterator<Element> iterator)
    {
        iterators.add(iterator);
    }

    public void addAll(final Collection<Iterator<Element>> iterators)
    {
        this.iterators.addAll(iterators);
    }

    @Override
    protected Element onNext()
    {
        if (index < iterators.size())
        {
            final var iterator = iterators.get(index);
            if (iterator.hasNext())
            {
                return iterator.next();
            }
            else
            {
                index++;
                return findNext();
            }
        }
        return null;
    }
}
