////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.set.logical.operations;

import com.telenav.kivakit.core.collections.set.logical.LogicalSet;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;

import java.util.Iterator;
import java.util.Set;

/**
 * The logical intersection of two sets. The two sets are combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the intersection, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class Intersection<T> extends LogicalSet<T>
{
    private final Set<T> larger;

    private final Set<T> smaller;

    public Intersection(final Set<T> a, final Set<T> b)
    {
        if (a.size() > b.size())
        {
            larger = a;
            smaller = b;
        }
        else
        {
            larger = b;
            smaller = a;
        }
    }

    @Override
    public boolean contains(final Object object)
    {
        return larger.contains(object) && smaller.contains(object);
    }

    @Override
    public boolean isEmpty()
    {
        for (final var object : smaller)
        {
            if (larger.contains(object))
            {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            private final Iterator<T> smallerIterator = smaller.iterator();

            @Override
            protected T onNext()
            {
                while (smallerIterator.hasNext())
                {
                    final var next = smallerIterator.next();
                    if (larger.contains(next))
                    {
                        return next;
                    }
                }
                return null;
            }
        };
    }

    @Override
    public int size()
    {
        var size = 0;
        for (final var object : smaller)
        {
            if (larger.contains(object))
            {
                size++;
            }
        }
        return size;
    }
}
