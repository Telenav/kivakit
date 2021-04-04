////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.bits;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A really simple, fixed-length bit array.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public class FixedSizeBitArray
{
    private long[] words;

    public FixedSizeBitArray(final int bits)
    {
        words = new long[((bits - 1) / 64) + 1];
    }

    private FixedSizeBitArray()
    {
    }

    public void clear(final int bitIndex)
    {
        words[bitIndex / 64] &= ~(1L << (bitIndex % 64));
    }

    public boolean get(final int bitIndex)
    {
        return (words[bitIndex / 64] & (1L << (bitIndex % 64))) != 0;
    }

    public void set(final int bitIndex)
    {
        words[bitIndex / 64] |= (1L << (bitIndex % 64));
    }

    public void set(final int bitIndex, final boolean value)
    {
        if (value)
        {
            set(bitIndex);
        }
        else
        {
            clear(bitIndex);
        }
    }
}
