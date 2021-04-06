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
 * A bit reader can read a single bit with {@link #readBit()} or up to 64 bits with {@link #readLong(int)}. The method
 * {@link #hasNext()} returns true if there is at least one more bit to read and {@link #close()} closes the source of
 * bits using the method implemented in {@link #onClose()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArrayBitIo.class)
public interface BitReader extends AutoCloseable
{
    @Override
    default void close()
    {
        onClose();
    }

    /**
     * @return The current read cursor position
     */
    long cursor();

    /**
     * @param position The new read cursor position
     */
    void cursor(long position);

    /** True if this reader has at least one more bit to read */
    boolean hasNext();

    /** Called to close the reader */
    void onClose();

    default int read(final int bits)
    {
        return (int) readLong(bits);
    }

    /**
     * @return The next bit read
     */
    boolean readBit();

    /**
     * Reads the next bit and then reads a value with the given small bit count if the bit is true, otherwise reads the
     * given big bit count.
     */
    int readFlexibleInt(final int smallBitCount, final int bigBitCount);

    /**
     * @return A long value of the specified size in bits
     */
    default long readLong(final int bits)
    {
        var value = 0L;
        var mask = 1L << (bits - 1);
        for (var i = 0; i < bits; i++)
        {
            value |= readBit() ? mask : 0;
            mask >>>= 1;
        }
        return value;
    }

    default String readString()
    {
        var length = (int) readLong(8);
        if (length == BitWriter.EXTENDED_LENGTH)
        {
            length = (int) readLong(16);
        }
        final var bytes = new byte[length];
        for (var i = 0; i < length; i++)
        {
            bytes[i] = (byte) readLong(8);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * @return The size of the data being read in bits, or -1 if it is unknown
     */
    default int size()
    {
        return -1;
    }
}
