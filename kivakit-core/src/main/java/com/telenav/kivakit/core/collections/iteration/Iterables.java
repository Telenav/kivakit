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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Utility methods that operate on {@link Iterable}s.
 *
 * <p><b>Hash/Equals</b></p>
 *
 * <ul>
 *     <li>{@link #hashCode(Iterable)}</li>
 *     <li>{@link #equals(Iterable, Iterable)}</li>
 * </ul>
 *
 * <p><b>Size</b></p>
 *
 * <ul>
 *     <li>{@link #size(Iterable)}</li>
 *     <li>{@link #isEmpty(Iterable)}</li>
 * </ul>
 *
 * <p><b>Construction</b></p>
 *
 * <ul>
 *     <li>{@link #iterable(Factory)}</li>
 *     <li>{@link #iterable(Object[])}</li>
 *     <li>{@link #emptyIterable()}</li>
 *     <li>{@link #singletonIterable(Object)}</li>
 * </ul>
 * <p>
 * The method {@link #iterable(Factory)} can be used to implement the
 * {@link Iterable} interface with a minimum of code. The implementation of the {@link NextIterator} interface provides
 * either the next value in an iteration of the sequence or null if there is none.
 * <pre>
 * var iterable = Iterables.iterable(() -&gt; new NextIterator&lt;Integer&gt;()
 * {
 *     int next;
 *
 *     public Integer onNext()
 *     {
 *         [...]
 *     }
 * }</pre>
 *
 * @author jonathanl (shibo)
 * @see NextIterator
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramIteration.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
     * @return True if the two sequences are equal
     */
    public static <T> boolean equals(Iterable<T> a, Iterable<T> b)
    {
        if (a != null && b != null)
        {
            return Iterators.equals(a.iterator(), b.iterator());
        }
        return a == null && b == null;
    }

    /**
     * @return A hash code for the objects in a sequence
     */
    public static <T> int hashCode(Iterable<T> iterable)
    {
        return Iterators.hashCode(iterable.iterator());
    }

    /**
     * @return True if the given iterable has no values
     */
    public static boolean isEmpty(@NotNull Iterable<?> iterable)
    {
        return !iterable.iterator().hasNext();
    }

    /**
     * @return An iterable for the given array of values
     */
    public static <T> Iterable<T> iterable(T[] values)
    {
        return Arrays.asList(values);
    }

    /**
     * @return An iterable for the given {@link NextIterator} factory
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
     * Returns an iterator that provides the given value as the only value in the sequence
     *
     * @param value The singleton value
     */
    public static <T> Iterable<T> singletonIterable(T value)
    {
        return () -> Iterators.singletonIterator(value);
    }

    /**
     * @param iterable An iterable
     * @return The number of items in this iterable
     */
    public static int size(Iterable<?> iterable)
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
}
