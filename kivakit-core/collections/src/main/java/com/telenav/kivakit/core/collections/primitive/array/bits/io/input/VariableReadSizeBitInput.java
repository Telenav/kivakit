////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.bits.io.input;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayBitIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

@UmlClassDiagram(diagram = DiagramPrimitiveArrayBitIo.class)
public class VariableReadSizeBitInput extends BitInput
{
    public VariableReadSizeBitInput(final InputStream in)
    {
        super(in);
    }

    /**
     * NGX has a special way of reading signed values of various sizes in bytes (up to long) by looking at the highest
     * bit and keeping reading if this bit is 1
     *
     * @return The value read by the method, cast to long
     * @see "shared/ngcc/varint.h -> int64_t readSigned(FILE* f)"
     */
    public long readSigned()
    {
        var value = 0L;
        short shift = 0;
        byte at;
        while (true)
        {
            at = (byte) read(8);
            if ((at & 0x80) == 0)
            {
                value |= ((long) (((byte) (at << 1)) >> 1)) << shift;
                return value;
            }
            value |= (((long) (at & 0x007F)) << shift);
            shift += 7;
        }
    }

    /**
     * NGX has a special way of reading unsigned values of various sizes in bytes (up to long) by looking at the highest
     * bit and keeping reading if this bit is 1
     *
     * @return The value read by the method, cast to long
     * @see "shared/ngcc/varint.h -> uint64_t readUnsigned(FILE* f)"
     */
    public long readUnsigned()
    {
        var value = 0L;
        short shift = 0;
        short at; // Unsigned byte (cast to a Short to avoid negative values in Java)
        do
        {
            at = (short) read(8);
            value |= (((long) (at & 0x007F)) << shift);
            shift += 7;
        }
        while ((at & 0x0080) != 0);
        return value;
    }
}
