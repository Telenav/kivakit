////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.iteration;

import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.language.objects.Objects;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Utility methods that operate on {@link Iterator}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIteration.class)
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
