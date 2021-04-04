////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.iteration.iterables;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterable;
import com.telenav.kivakit.core.kernel.language.iteration.Next;

import java.util.Iterator;

/**
 * An {@link Iterable} that wraps and filters the elements of another {@link Iterable} producing only elements that
 * match the {@link Matcher} given to the constructor.
 *
 * @author jonathanl (shibo)
 * @see BaseIterable
 * @see Filter
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public class FilteredIterable<Element> extends BaseIterable<Element>
{
    private final Matcher<Element> filter;

    private final Iterable<Element> iterable;

    public FilteredIterable(final Iterable<Element> iterable, final Matcher<Element> filter)
    {
        this.iterable = iterable;
        this.filter = filter;
    }

    @Override
    protected Next<Element> newNext()
    {
        return new Next<>()
        {
            private final Iterator<Element> iterator = iterable.iterator();

            @Override
            public Element onNext()
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
        };
    }
}
