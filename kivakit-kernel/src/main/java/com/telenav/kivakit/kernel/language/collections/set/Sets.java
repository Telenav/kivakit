////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.collections.set;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.factory.Factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Function;

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
