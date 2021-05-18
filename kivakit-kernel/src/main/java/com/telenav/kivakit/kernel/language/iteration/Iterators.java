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

package com.telenav.kivakit.kernel.language.iteration;

import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageIteration;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Utility methods that operate on {@link Iterator}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIteration.class)
@LexakaiJavadoc(complete = true)
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
    public static <T> boolean equals(final Iterator<T> iteratorA, final Iterator<T> iteratorB)
    {
        while (true)
        {
            final var aHasNext = iteratorA.hasNext();
            final var bHasNext = iteratorB.hasNext();
            if (aHasNext && bHasNext)
            {
                final var a = iteratorA.next();
                final var b = iteratorB.next();
                if (!Objects.equal(a, b))
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
    public static <T> int hashCode(final Iterator<T> iterator)
    {
        var hashCode = 1;
        while (iterator.hasNext())
        {
            final var next = iterator.next();
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
    public static <T> Iterator<T> iterator(final Supplier<T> supplier)
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
}
