////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array;

import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.IntArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.LongArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.ShortArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitIntArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitLongArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

/**
 * Base class for primitive arrays.
 *
 * @author jonathanl (shibo)
 * @see ByteArray
 * @see IntArray
 * @see ShortArray
 * @see LongArray
 * @see PrimitiveSplitArray
 * @see SplitByteArray
 * @see SplitIntArray
 * @see SplitLongArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public abstract class PrimitiveArray extends PrimitiveCollection
{
    protected PrimitiveArray(final String name)
    {
        super(name);
    }

    protected PrimitiveArray()
    {
    }

    @Override
    public Count capacity()
    {
        return count();
    }
}
