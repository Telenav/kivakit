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
import com.telenav.kivakit.core.kernel.language.objects.Hash;

import java.util.Iterator;
import java.util.Set;

/**
 * The logical union of two sets. The two sets are combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the union, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class Union<T> extends LogicalSet<T>
{
    private final Set<T> larger;

    private final Set<T> smaller;

    public Union(final Set<T> a, final Set<T> b)
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
        return larger.contains(object) || smaller.contains(object);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Set)
        {
            final var that = (Set<?>) object;
            if (size() == that.size())
            {
                for (final Object value : this)
                {
                    if (!that.contains(value))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(larger, smaller);
    }

    @Override
    public boolean isEmpty()
    {
        return larger.isEmpty() && smaller.isEmpty();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            private final Iterator<T> largerIterator = larger.iterator();

            private final Iterator<T> smallerIterator = smaller.iterator();

            @Override
            protected T onNext()
            {
                // First we iterate through all the larger set
                if (largerIterator.hasNext())
                {
                    return largerIterator.next();
                }

                // Then we iterate through all the members of the smaller set,
                while (smallerIterator.hasNext())
                {
                    final var next = smallerIterator.next();

                    // excluding any values that were returned from the larger set
                    if (!larger.contains(next))
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
        var size = larger.size();
        for (final var object : smaller)
        {
            if (!larger.contains(object))
            {
                size++;
            }
        }
        return size;
    }
}
