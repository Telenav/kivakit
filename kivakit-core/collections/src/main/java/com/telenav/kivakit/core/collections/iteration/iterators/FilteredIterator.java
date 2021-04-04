////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.iteration.iterators;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;

import java.util.Iterator;

/**
 * An {@link Iterator} that wraps and filters the values of another {@link Iterator}.
 *
 * @author jonathanl (shibo)
 * @see BaseIterator
 * @see Filter
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class FilteredIterator<Element> extends BaseIterator<Element>
{
    private final Iterator<Element> iterator;

    private final Matcher<Element> filter;

    public FilteredIterator(final Iterator<Element> iterator, final Matcher<Element> filter)
    {
        this.iterator = iterator;
        this.filter = filter;
    }

    @Override
    protected Element onNext()
    {
        while (iterator.hasNext())
        {
            final var next = iterator.next();
            if (filter.matches(next))
            {
                return next;
            }
        }
        return null;
    }
}
