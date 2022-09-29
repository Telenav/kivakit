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

package com.telenav.kivakit.core.collections;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.interfaces.factory.Factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Set utility methods for working on sets. Prefer {@link ObjectSet} when possible.
 *
 * <p><b>Construction</b></p>
 *
 * <ul>
 *     <li>{@link #empty()}</li>
 *     <li>{@link #hashSet(Object[])}</li>
 *     <li>{@link #hashSet(Iterable)}</li>
 *     <li>{@link #hashSet(Collection)}</li>
 * </ul>
 *
 * <p><b>Copying</b></p>
 *
 * <ul>
 *     <li>{@link #copy(Factory, Set)}</li>
 *     <li>{@link #deepCopy(Factory, Set, Function)}</li>
 * </ul>
 *
 * <p><b>Other</b></p>
 *
 * <ul>
 *     <li>{@link #first(Set)}</li>
 *     <li>{@link #union(Set, Set)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_STATIC_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class Sets
{
    /**
     * @return A copy of the given set
     */
    public static <Value> Set<Value> copy(Factory<Set<Value>> factory,
                                          Set<Value> set)
    {
        return deepCopy(factory, set, value -> value);
    }

    /**
     * @return A copy of the given set
     */
    public static <Value> Set<Value> deepCopy(Factory<Set<Value>> factory,
                                              Set<Value> set,
                                              Function<Value, Value> clone)
    {
        var copy = factory.newInstance();
        for (var value : set)
        {
            Ensure.ensureNotNull(value);
            var clonedValue = clone.apply(value);
            Ensure.ensureNotNull(clonedValue);
            copy.add(clonedValue);
        }
        return copy;
    }

    /**
     * Returns the empty set.
     */
    public static <Value> Set<Value> empty()
    {
        return Collections.emptySet();
    }

    /**
     * Returns the first available value in the set or null. Which value is returned is not defined.
     */
    public static <Value> Value first(Set<Value> set)
    {
        return set.isEmpty() ? null : set.iterator().next();
    }

    /**
     * Constructs a hash set from an iterable
     */
    public static <T> Set<T> hashSet(Iterable<T> iterable)
    {
        var set = new HashSet<T>();
        iterable.forEach(set::add);
        return set;
    }

    /**
     * Constructs a hash set from a collection
     */
    public static <T> HashSet<T> hashSet(Collection<T> collection)
    {
        return new HashSet<>(collection);
    }

    /**
     * Constructs a hash set from a variable number of values
     */
    @SafeVarargs
    public static <T> Set<T> hashSet(T... values)
    {
        return new HashSet<>(Arrays.asList(values));
    }

    /**
     * Returns the union of the two given sets
     */
    public static <T> Set<T> union(Set<T> a, Set<T> b)
    {
        var union = new HashSet<T>();
        union.addAll(a);
        union.addAll(b);
        return union;
    }
}
