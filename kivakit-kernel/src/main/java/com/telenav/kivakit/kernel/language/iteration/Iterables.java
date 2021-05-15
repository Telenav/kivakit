////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.iteration;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageIteration;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Utility methods that operate on {@link Iterable}s. The method {@link #iterable(Factory)} can be used to implement the
 * {@link Iterable} interface with a minimum of code. The implementation of the {@link Next} interface provides either
 * the next value in an iteration of the sequence or null if there is none.
 * <pre>
 * var iterable = Iterables.of(() -&gt; new Next&lt;Integer&gt;()
 * {
 *     int next;
 *
 *     public Integer onNext()
 *     {
 *         [...]
 *     }
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Next
 */
@UmlClassDiagram(diagram = DiagramLanguageIteration.class)
public class Iterables
{
    public static <T> void addAll(final Iterable<T> iterable, final Collection<? super T> collection)
    {
        for (final var value : iterable)
        {
            collection.add(value);
        }
    }

    public static <T> boolean contains(final Iterable<T> iterable, final T value)
    {
        for (final var next : iterable)
        {
            if (next.equals(value))
            {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean containsAny(final Iterable<T> iterable, final Set<T> values)
    {
        for (final var next : iterable)
        {
            if (values.contains(next))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True if the two sequences are equal
     */
    public static <T> boolean equals(final Iterable<T> a, final Iterable<T> b)
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
    public static <T> int hashCode(final Iterable<T> iterable)
    {
        return Iterators.hashCode(iterable.iterator());
    }

    public static boolean isEmpty(final Iterable<?> iterable)
    {
        if (iterable == null)
        {
            return true;
        }
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).isEmpty();
        }
        return !iterable.iterator().hasNext();
    }

    /**
     * @return An iterable for the given {@link Next} factory
     */
    public static <T> Iterable<T> iterable(final Factory<Next<T>> factory)
    {
        return new BaseIterable<>()
        {
            @Override
            protected Next<T> newNext()
            {
                return factory.newInstance();
            }
        };
    }

    /**
     * @param iterable An iterable
     * @return The number of items in this iterable
     */
    public static <T> int size(final Iterable<T> iterable)
    {
        if (iterable instanceof List)
        {
            return ((List<T>) iterable).size();
        }
        var counter = 0;
        for (final var ignored : iterable)
        {
            counter++;
        }
        return counter;
    }

    public static <T> StringList toString(final Iterable<T> iterable)
    {
        final var result = new StringList();
        for (final var value : iterable)
        {
            result.add(value.toString());
        }
        return result;
    }
}