////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.iteration;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.objects.Hash;

/**
 * Base class for primitive iterators, allowing subclasses to be treated the same
 */
@UmlClassDiagram(diagram = DiagramPrimitiveCollection.class)
public interface PrimitiveIterator
{
    /** True if there is a next value */
    boolean hasNext();

    default int hashValue()
    {
        long hash = Hash.SEED;
        while (hasNext())
        {
            hash = hash ^ nextLong();
        }
        return (int) hash;
    }

    default boolean identical(final PrimitiveIterator that)
    {
        while (hasNext())
        {
            if (!that.hasNext())
            {
                return false;
            }
            if (nextLong() != that.nextLong())
            {
                return false;
            }
        }
        return !that.hasNext();
    }

    /** The next value as a long (even if the subclass is iterating another primitive type */
    long nextLong();
}
