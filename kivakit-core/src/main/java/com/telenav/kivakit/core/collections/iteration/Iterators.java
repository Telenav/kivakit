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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Hash.HASH_SEED;

/**
 * Utility methods that operate on {@link Iterator}s.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #iterator(Supplier)}</li>
 *     <li>{@link #emptyIterator()} </li>
 *     <li>{@link #singletonIterator(Object)}</li>
 * </ul>
 *
 * <p><b>Equality</b></p>
 *
 * <ul>
 *     <li>{@link #iteratorHashCode(Iterator)}</li>
 *     <li>{@link #equalIterators(Iterator, Iterator)}</li>
 * </ul>
 *
 * <p><b>Size</b></p>
 *
 * <ul>
 *     <li>{@link #iteratorSize(Iterator)}</li>
 *     <li>{@link #isEmptyIterator(Iterator)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramIteration.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Iterators
{

    /**
     * Returns an empty iterator
     */
    public static <T> Iterator<T> emptyIterator()
    {
        return new Iterator<>()
        {
            @Override
            public boolean hasNext()
            {
                return false;
            }

            @Override
            public T next()
            {
                return null;
            }
        };
    }

    /**
     * Returns true if the two sequences are equal
     */
    public static <T> boolean equalIterators(Iterator<T> a, Iterator<T> b)
    {
        while (true)
        {
            var aHasNext = a.hasNext();
            var bHasNext = b.hasNext();
            if (aHasNext && bHasNext)
            {
                if (!Objects.equals(a.next(), b.next()))
                {
                    return false;
                }
            }
            else
            {
                return !aHasNext && !bHasNext;
            }
        }
    }

    /**
     * Returns a hash code for the objects in a sequence
     */
    public static <T> int iteratorHashCode(Iterator<T> iterator)
    {
        var hashCode = 1;
        while (iterator.hasNext())
        {
            var next = iterator.next();
            if (next != null)
            {
                hashCode = hashCode * HASH_SEED + next.hashCode();
            }
        }
        return hashCode;
    }

    /**
     * Returns true if the given iterable has no values
     */
    public static boolean isEmptyIterator(@NotNull Iterator<?> iterator)
    {
        return !iterator.hasNext();
    }

    /**
     * Returns an iterator that provides more values until the supplier returns null
     */
    public static <T> Iterator<T> iterator(Supplier<T> supplier)
    {
        return new BaseIterator<>()
        {
            @Override
            protected T onNext()
            {
                return supplier.get();
            }
        };
    }

    /**
     * Returns an iterator that provides the given value as the only value in the sequence
     *
     * @param value The singleton value
     */
    public static <T> Iterator<T> singletonIterator(T value)
    {
        return new BaseIterator<>()
        {
            int index = 0;

            @Override
            protected T onNext()
            {
                if (index++ == 0)
                {
                    return value;
                }
                return null;
            }
        };
    }

    /**
     * @param iterator An iterator
     * @return The number of items produced by this iterator
     */
    public static int iteratorSize(Iterator<?> iterator)
    {
        var counter = 0;
        while (iterator.hasNext())
        {
            iterator.next();
            counter++;
        }
        return counter;
    }
}
