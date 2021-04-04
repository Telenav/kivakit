////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array;

import com.telenav.kivakit.core.collections.primitive.array.arrays.ByteArrayArray;
import com.telenav.kivakit.core.collections.primitive.array.arrays.IntArrayArray;
import com.telenav.kivakit.core.collections.primitive.array.arrays.LongArrayArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for two-dimensional primitive arrays.
 *
 * @author jonathanl (shibo)
 * @see ByteArrayArray
 * @see IntArrayArray
 * @see LongArrayArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArrayArray.class)
public abstract class PrimitiveArrayArray extends PrimitiveArray
{
    protected PrimitiveArrayArray(final String name)
    {
        super(name);
    }

    protected PrimitiveArrayArray()
    {
    }
}
