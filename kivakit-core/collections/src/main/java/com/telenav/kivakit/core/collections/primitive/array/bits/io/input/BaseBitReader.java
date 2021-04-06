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

package com.telenav.kivakit.core.collections.primitive.array.bits.io.input;

import com.telenav.kivakit.core.collections.primitive.array.bits.BitArray;
import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitReader;
import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayBitIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.strings.StringTo;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.messaging.Message;

/**
 * Reads bits from some sequence of bytes.
 *
 * @author jonathanl (shibo)
 * @see BitInput
 * @see BitArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArrayBitIo.class)
public abstract class BaseBitReader implements BitReader
{
    /** The current byte value */
    private byte current;

    /** The current bit mask */
    private int mask;

    /**
     * The position of the read cursor in bits
     */
    private long cursor;

    /** Current position in byte list */
    private int byteCursor;

    /** The list of bytes to read bits from */
    private ByteList bytes;

    /** The total number of bits in the input stream or -1 if it is unknown */
    private final int size;

    /**
     * @param bytes List of bytes to read from
     * @param size The number of bits stored in the bytes array
     */
    protected BaseBitReader(final ByteList bytes, final Count size)
    {
        this.bytes = bytes;
        this.size = size.asInt();
    }

    protected BaseBitReader()
    {
        size = -1;
    }

    /**
     * @return The current bit position in the input
     */
    @Override
    public long cursor()
    {
        return cursor;
    }

    /**
     * Seeks to the given bit index
     */
    @Override
    public void cursor(final long index)
    {
        // Set the bit cursor,
        cursor = index;

        // and the byte cursor,
        byteCursor(index / 8);

        // read the next byte to prime things
        current = nextByte();

        // then set the mask based on the bit index
        mask = 0x80 >>> (index % 8);
    }

    /**
     * @return True if there is at least one more bit to read
     */
    @Override
    public boolean hasNext()
    {
        // Get the size of the entire bitstream (or -1 if it is unknown, as in the case of an InputStream)
        final var size = size();

        // and if the size is unknown, we have a next if we haven't finished the current byte or if there is
        // a next one from input stream lookahead, otherwise if we know the size, we have a next bit if the
        // cursor (in bits) is less than the size in bits.
        return size == -1 ? mask != 0 || hasNextByte() : cursor < size;
    }

    @Override
    public void onClose()
    {
    }

    /**
     * @return An integer of the specified size in bits
     */
    @Override
    public final int read(int bits)
    {
        var value = 0;
        var mask = 1 << (bits - 1);
        while (bits > 0)
        {
            if (this.mask == 0 && bits >= 8)
            {
                current = nextByte();
                value |= ((current & 0xff) << (bits - 8));
                mask >>>= 8;
                bits -= 8;
                cursor += bits;
            }
            else
            {
                value |= readBit() ? mask : 0;
                mask >>>= 1;
                bits--;
            }
        }
        return value;
    }

    /**
     * @return The next bit
     */
    @Override
    public final boolean readBit()
    {
        // If we don't have a value
        if (mask == 0)
        {
            // then read one
            current = nextByte();
            mask = 0x80;
        }

        // get the bit
        final var value = (current & mask) != 0;

        // and advance to the next bit
        mask >>>= 1;
        cursor++;

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int readFlexibleInt(final int smallBitCount, final int bigBitCount)
    {
        if (readBit())
        {
            return read(smallBitCount);
        }
        else
        {
            return read(bigBitCount);
        }
    }

    /**
     * @return A long value of the specified size in bits
     */
    @Override
    public final long readLong(final int bits)
    {
        var value = 0L;
        var mask = 1L << (bits - 1);
        for (var i = 0; i < bits; i++)
        {
            value |= readBit() ? mask : 0;
            mask >>>= 1;
        }
        cursor += bits;
        return value;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return Message.format("[BitReader cursor = $, size = $, hasNext = $, current = $, mask = $]",
                cursor(), size(), hasNext(), StringTo.binary(current, 8), mask);
    }

    /**
     * @return The position (in bytes) of the next byte that will be read
     */
    protected long byteCursor()
    {
        return byteCursor;
    }

    /**
     * Sets the position (in bytes) of the next byte that will be read
     */
    protected void byteCursor(final long position)
    {
        byteCursor = (int) position;
    }

    /**
     * @return True if the underlying byte stream has a next byte
     */
    protected boolean hasNextByte()
    {
        return byteCursor < bytes.size();
    }

    /**
     * @return The current bit mask
     */
    protected final int mask()
    {
        return mask;
    }

    /**
     * Reads the next byte, if any
     */
    protected byte nextByte()
    {
        assert hasNextByte() : "Out of input";
        return bytes.get(byteCursor++);
    }
}
