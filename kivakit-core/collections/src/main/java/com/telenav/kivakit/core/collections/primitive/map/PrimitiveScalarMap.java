////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map;

import com.telenav.kivakit.core.collections.primitive.map.scalars.IntToByteMap;
import com.telenav.kivakit.core.collections.primitive.map.scalars.LongToLongMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A map from one scalar primitive to another, like {@link LongToLongMap} or {@link IntToByteMap}. This interface allows
 * all such maps to be treated the same, by widening the key and value to a long primitive.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public interface PrimitiveScalarMap
{
    /**
     * @return The value for the given key
     */
    long getScalar(long key);

    /**
     * @return True if the given key represents null
     */
    boolean isScalarKeyNull(long key);

    /**
     * @return True if the given value represents null
     */
    boolean isScalarValueNull(long value);

    /**
     * Stores the given value under the given key
     */
    void putScalar(long key, long value);

    default void putScalar(final long key, final Quantizable value)
    {
        putScalar(key, value.quantum());
    }
}
