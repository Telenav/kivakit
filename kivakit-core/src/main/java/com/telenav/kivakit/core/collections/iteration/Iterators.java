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
import com.telenav.kivakit.core.language.Hash;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Utility methods that operate on {@link Iterator}s.
 *
 * <p><b>Hash/Equals</b></p>
 *
 * <ul>
 *     <li>{@link #hashCode(Iterator)}</li>
 *     <li>{@link #equals(Iterator, Iterator)}</li>
 * </ul>
 *
 * <p><b>Size</b></p>
 *
 * <ul>
 *     <li>{@link #size(Iterator)}</li>
 *     <li>{@link #isEmpty(Iterator)}</li>
 * </ul>
 *
 * <p><b>Construction</b></p>
 *
 * <ul>
 *     <li>{@link #iterator(Supplier)}</li>
 *     <li>{@link #emptyIterator()} </li>
 *     <li>{@link #singletonIterator(Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
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
     * @return True if the two sequences are equal
     */
    public static <T> boolean equals(Iterator<T> a, Iterator<T> b)
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
     * @return A hash code for the objects in a sequence
     */
    public static <T> int hashCode(Iterator<T> iterator)
    {
        var hashCode = 1;
        while (iterator.hasNext())
        {
            var next = iterator.next();
            if (next != null)
            {
                hashCode = hashCode * Hash.SEED + next.hashCode();
            }
        }
        return hashCode;
    }

    /**
     * @return True if the given iterable has no values
     */
    public static boolean isEmpty(@NotNull Iterator<?> iterator)
    {
        return !iterator.hasNext();
    }

    /**
     * @return An iterator that provides more values until the supplier returns null
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
    public static int size(Iterator<?> iterator)
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
