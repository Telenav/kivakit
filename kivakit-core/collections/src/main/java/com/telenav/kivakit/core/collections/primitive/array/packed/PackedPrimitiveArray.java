////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.packed;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;

/**
 * A bit-packed array of primitive values
 *
 * @author jonathanl (shibo)
 * @see PackedArray
 * @see SplitPackedArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public interface PackedPrimitiveArray
{
    /**
     * OverflowHandling handling specifier
     */
    enum OverflowHandling
    {
        /** Allow overflow to happen */
        ALLOW_OVERFLOW,

        /** Limit values to the maximum value for the number of bits in this array */
        NO_OVERFLOW
    }

    /**
     * @param bits The number of bits of storage per element in the array
     * @param overflow How to handle values that are too large to be stored in the given number of bits
     * @return The array itself
     */
    PackedPrimitiveArray bits(BitCount bits, OverflowHandling overflow);

    /**
     * @return The number of bits per element in this packed array
     */
    BitCount bits();

    /**
     * @return Directive on how to handle values too large for the bit-width of the array
     */
    OverflowHandling overflow();
}
