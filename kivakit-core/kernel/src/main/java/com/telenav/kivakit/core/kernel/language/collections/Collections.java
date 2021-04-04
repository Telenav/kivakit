////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Collection utility methods
 *
 * @author jonathanl (shibo)
 */
public class Collections
{
    public static <T> T first(final Collection<T> collection)
    {
        final var iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public static <T> void repeatedAdd(final Collection<T> collection, final T value, final int times)
    {
        for (var index = 0; index < times; index++)
        {
            collection.add(value);
        }
    }

    public static <T extends Comparable<T>> List<T> sorted(final Collection<T> collection)
    {
        final var list = new ArrayList<>(collection == null ? Set.of() : collection);
        java.util.Collections.sort(list);
        return list;
    }

    public <T> List<T> toList(final Collection<T> collection)
    {
        return new ArrayList<>(collection);
    }

    public <T> Set<T> toSet(final Collection<T> collection)
    {
        return new HashSet<>(collection);
    }
}
