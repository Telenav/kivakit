////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.bits.io;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayBitIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.nio.charset.StandardCharsets;

@UmlClassDiagram(diagram = DiagramPrimitiveArrayBitIo.class)
public interface BitWriter extends AutoCloseable
{
    int EXTENDED_LENGTH = 255;
    int MAXIMUM_LENGTH = 0xffff;

    @Override
    default void close()
    {
        onClose();
    }

    /**
     * @return The current position in bits
     */
    long cursor();

    /** Flushes any unwritten bits, writing the last byte to the output */
    void flush();

    /** For subclass to handle closing the output */
    void onClose();

    default void write(final long value, final int bits)
    {
        var mask = 1L << (bits - 1);
        for (var i = 0; i < bits; i++)
        {
            writeBit((value & mask) != 0);
            mask >>>= 1;
        }
    }

    /**
     * @return True if writing the given bit caused a byte to be written
     */
    boolean writeBit(boolean bit);

    default void writeByte(final byte value)
    {
        write(value, 8);
    }

    /**
     * Writes the given value using the small bit count if it will fit, otherwise writes it using the big bit account.
     * The data written by this method can be read with {@link BitReader#readFlexibleInt(int, int)}
     */
    void writeFlexibleInt(final int smallBitCount, final int bigBitCount, final int value);

    default void writeInt(final int value)
    {
        write(value, 32);
    }

    default void writeString(final String value)
    {
        final var bytes = value.getBytes(StandardCharsets.UTF_8);
        final var length = Math.min(MAXIMUM_LENGTH, bytes.length);
        if (length < EXTENDED_LENGTH)
        {
            write(length, 8);
        }
        else
        {
            write(EXTENDED_LENGTH, 8);
            write(length, 16);
        }
        for (var i = 0; i < length; i++)
        {
            write(bytes[i], 8);
        }
    }
}
