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
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.iteration.BaseIterator;

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
public class Subset<T> extends LogicalSet<T>
{
    private final Set<T> set;

    private final Matcher<T> matcher;

    public Subset(final Set<T> set, final Matcher<T> matcher)
    {
        this.set = set;
        this.matcher = matcher;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object object)
    {
        return set.contains(object) && matcher.matches((T) object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Set)
        {
            final var that = (Set<T>) object;
            if (size() == that.size())
            {
                for (final var value : this)
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
        return set.hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        if (set.isEmpty())
        {
            return true;
        }
        for (final var member : set)
        {
            if (matcher.matches(member))
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
            private final Iterator<T> iterator = set.iterator();

            @Override
            protected T onNext()
            {
                while (iterator.hasNext())
                {
                    final var next = iterator.next();
                    if (matcher.matches(next))
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
        for (final var member : set)
        {
            if (matcher.matches(member))
            {
                size++;
            }
        }
        return size;
    }
}
