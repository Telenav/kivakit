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
 * A given set without the elements of another set (technically, the "relative complement"). The two sets are logically
 * combined without creating any new set.
 * <p>
 * This set is not modifiable. To change the set, you must modify the underlying set(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class Without<T> extends LogicalSet<T>
{
    private final Set<T> set;

    private final Set<T> without;

    /**
     * @param set The base set
     * @param without The set whose elements should be excluded from the base set
     */
    public Without(final Set<T> set, final Set<T> without)
    {
        this.set = set;
        this.without = without;
    }

    @Override
    public boolean contains(final Object object)
    {
        return set.contains(object) && !without.contains(object);
    }

    @Override
    public boolean isEmpty()
    {
        if (set.isEmpty())
        {
            return true;
        }
        for (final var object : set)
        {
            if (!without.contains(object))
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
                    if (!without.contains(next))
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
        var size = set.size();
        for (final var object : without)
        {
            if (set.contains(object))
            {
                size--;
            }
        }
        return size;
    }
}
