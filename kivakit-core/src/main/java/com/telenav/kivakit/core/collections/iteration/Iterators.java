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

import com.telenav.kivakit.annotations.code.ApiStability;
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Utility methods that operate on {@link Iterator}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
@ApiQuality(stability = ApiStability.STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Iterators
{
    public static <T> Iterator<T> empty()
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
    public static <T> boolean equals(Iterator<T> iteratorA, Iterator<T> iteratorB)
    {
        while (true)
        {
            var aHasNext = iteratorA.hasNext();
            var bHasNext = iteratorB.hasNext();
            if (aHasNext && bHasNext)
            {
                var a = iteratorA.next();
                var b = iteratorB.next();
                if (!Objects.equals(a, b))
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
}
