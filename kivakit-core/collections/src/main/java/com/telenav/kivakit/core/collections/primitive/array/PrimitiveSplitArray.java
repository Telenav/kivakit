////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array;

import com.telenav.kivakit.core.collections.primitive.array.packed.SplitPackedArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitCharArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitIntArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitLongArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveSplitArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for "split" primitive arrays. Split collections manage storage with several children instead of large
 * allocations, which can be expensive to resize and hard on the garbage collector.
 *
 * @author jonathanl (shibo)
 * @see SplitByteArray
 * @see SplitCharArray
 * @see SplitIntArray
 * @see SplitLongArray
 * @see SplitPackedArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveSplitArray.class)
public abstract class PrimitiveSplitArray extends PrimitiveArray
{
    protected PrimitiveSplitArray(final String name)
    {
        super(name);
    }

    protected PrimitiveSplitArray()
    {
    }
}
