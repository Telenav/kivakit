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
 * An iterator that produces a sequence of values. More values are available so long as {@link #hasNext()} returns true.
 * The next value can be retrieved with {@link #next()}. A hash code for all the values in the sequence can be retrieved
 * with {@link #hash()}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveCollection.class)
public interface ByteIterator extends PrimitiveIterator
{
    /**
     * @return True if there is a next value
     */
    @Override
    boolean hasNext();

    /**
     * Computes a hash value for this iterator. Since iterators often produce the same values out of order (in sets and
     * maps), the algorithm takes this into account, producing the same hash value no matter the order of elements
     * iterated.
     *
     * @return A hash value composed by iterating through available values
     */
    default int hash()
    {
        var hashCode = 1;
        while (hasNext())
        {
            hashCode = hashCode + Hash.code(next());
        }
        return hashCode;
    }

    /**
     * @return The next value in the sequence
     */
    byte next();

    @Override
    default long nextLong()
    {
        return next();
    }
}
