////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.set;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Function;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Set utility methods.
 *
 * @author jonathanl (shibo)
 */
public class Sets
{
    /**
     * @return A copy of the given set
     */
    public static <Value> Set<Value> copy(final Factory<Set<Value>> factory,
                                          final Set<Value> set)
    {
        return deepCopy(factory, set, value -> value);
    }

    /**
     * @return A copy of the given set
     */
    public static <Value> Set<Value> deepCopy(final Factory<Set<Value>> factory,
                                              final Set<Value> set,
                                              final Function<Value, Value> clone)
    {
        final var copy = factory.newInstance();
        for (final var value : set)
        {
            Ensure.ensureNotNull(value);
            final var clonedValue = clone.apply(value);
            Ensure.ensureNotNull(clonedValue);
            copy.add(clonedValue);
        }
        return copy;
    }

    public static <T> Set<T> empty()
    {
        return Collections.emptySet();
    }

    public static <T> T first(final Set<T> set)
    {
        return set.isEmpty() ? null : set.iterator().next();
    }

    public static <T> Set<T> fromIterable(final Iterable<T> iterable)
    {
        final Set<T> set = new HashSet<>();
        iterable.forEach(set::add);
        return set;
    }

    public static <T> HashSet<T> hashset(final Collection<T> collection)
    {
        return new HashSet<>(collection);
    }

    @SafeVarargs
    public static <T> HashSet<T> hashset(final T... values)
    {
        return new HashSet<>(Set.of(values));
    }

    public static <T> Set<T> identitySet()
    {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }

    public static <T> Set<T> nonNull(final Set<T> set)
    {
        return set == null ? empty() : set;
    }

    @SafeVarargs
    public static <T> Set<T> of(final T... list)
    {
        return new HashSet<>(Arrays.asList(list));
    }

    public static <T> Set<T> union(final Set<T> a, final Set<T> b)
    {
        final var union = new HashSet<T>();
        union.addAll(a);
        union.addAll(b);
        return union;
    }
}
