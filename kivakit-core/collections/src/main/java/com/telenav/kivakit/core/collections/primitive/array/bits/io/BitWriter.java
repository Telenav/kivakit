////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.bits.io;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayBitIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.nio.charset.StandardCharsets;

/**
 * Interface for code that writes bits to some destination.
 *
 * <p>
 * Implementers must provide:
 * </p>
 *
 * <ul>
 *     <li>{@link #cursor()} - The current write position in bits</li>
 *     <li>{@link #flush()} - Flushes any unwritten data</li>
 *     <li>{@link #writeBit(boolean)} - Writes the given bit to output</li>
 *     <li>{@link #onClose()} - Closes the writer, flushing any data</li>
 * </ul>
 *
 * <p>
 * Default methods provide:
 * </p>
 *
 * <ul>
 *     <li>{@link #write(long, int)}</li>
 *     <li>{@link #writeByte(byte)}</li>
 *     <li>{@link #writeInt(int)}</li>
 *     <li>{@link #writeString(String)}</li>
 *     <li>{@link #writeFlexibleInt(int, int, int)}</li>
 * </ul>
 */
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
     * Writes the given value using the small bit count if it will fit, otherwise the big bit count
     */
    default void writeFlexibleInt(final int smallBitCount, final int bigBitCount, final int value)
    {
        assert smallBitCount < bigBitCount;
        final var isSmall = value < (1 << smallBitCount);
        writeBit(isSmall);
        if (isSmall)
        {
            write(value, smallBitCount);
        }
        else
        {
            write(value, bigBitCount);
        }
    }

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
