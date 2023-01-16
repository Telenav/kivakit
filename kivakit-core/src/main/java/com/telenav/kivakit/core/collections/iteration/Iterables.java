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

package com.telenav.kivakit.core.collections.iteration;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.iteration.Iterators.iteratorHashCode;
import static com.telenav.kivakit.core.collections.iteration.Iterators.singletonIterator;

/**
 * Utility methods that operate on {@link Iterable}s.
 *
 * <p>
 * The method {@link #iterable(Factory)} can be used to implement the {@link Iterable} interface with a minimum of code.
 * The implementation of the {@link NextIterator} interface provides either the next value in an iteration of the
 * sequence or null if there is none.
 * <pre>
 * var iterable = iterable(() -&gt; new NextIterator&lt;Integer&gt;()
 * {
 *     int next;
 *
 *     public Integer onNext()
 *     {
 *         [...]
 *     }
 * }</pre>
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #iterable(Factory)}</li>
 *     <li>{@link #iterable(Object[])}</li>
 *     <li>{@link #emptyIterable()}</li>
 *     <li>{@link #singletonIterable(Object)}</li>
 * </ul>
 *
 * <p><b>Equality</b></p>
 *
 * <ul>
 *     <li>{@link #iterableHashCode(Iterable)}</li>
 *     <li>{@link #equalIterables(Iterable, Iterable)}</li>
 * </ul>
 *
 * <p><b>Size</b></p>
 *
 * <ul>
 *     <li>{@link #iterableSize(Iterable)}</li>
 *     <li>{@link #isEmpty(Iterable)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see NextIterator
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramIteration.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Iterables
{
    /**
     * Returns an empty iterator
     */
    public static <T> Iterable<T> emptyIterable()
    {
        return Iterators::emptyIterator;
    }

    /**
     * Returns true if the two sequences are equal
     */
    public static <T> boolean equalIterables(Iterable<T> a, Iterable<T> b)
    {
        if (a != null && b != null)
        {
            return Iterators.equalIterators(a.iterator(), b.iterator());
        }
        return a == null && b == null;
    }

    /**
     * Returns true if the given iterable has no values
     */
    public static boolean isEmpty(@NotNull Iterable<?> iterable)
    {
        return !iterable.iterator().hasNext();
    }

    /**
     * Returns an iterable for the given array of values
     */
    public static <T> Iterable<T> iterable(T[] values)
    {
        return Arrays.asList(values);
    }

    /**
     * Returns an iterable for the given {@link NextIterator} factory
     */
    public static <T> Iterable<T> iterable(Factory<NextIterator<T>> factory)
    {
        return new BaseIterable<>()
        {
            @Override
            protected NextIterator<T> newNextIterator()
            {
                return factory.newInstance();
            }
        };
    }

    /**
     * Returns a hash code for the objects in a sequence
     */
    public static <T> int iterableHashCode(Iterable<T> iterable)
    {
        return iteratorHashCode(iterable.iterator());
    }

    /**
     * @param iterable An iterable
     * @return The number of items in this iterable
     */
    public static int iterableSize(Iterable<?> iterable)
    {
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).size();
        }
        var counter = 0;
        for (var ignored : iterable)
        {
            counter++;
        }
        return counter;
    }

    /**
     * Returns an iterator that provides the given value as the only value in the sequence
     *
     * @param value The singleton value
     */
    public static <T> Iterable<T> singletonIterable(T value)
    {
        return () -> singletonIterator(value);
    }
}
