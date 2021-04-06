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

package com.telenav.kivakit.core.collections.primitive.array.bits.io.output;

import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitWriter;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayBitIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Writes bits to some byte destination.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArrayBitIo.class)
public abstract class BaseBitWriter implements BitWriter
{
    /** The current value */
    private byte value;

    /** The current bit mask */
    private int mask = 0x80;

    /** The location of the write cursor */
    private long cursor;

    /**
     * @return The current bit position being written to
     */
    @Override
    public long cursor()
    {
        return cursor;
    }

    /**
     * Flushes any unwritten bits without advancing the output stream (not possible on bit writers like BitOutput)
     */
    @Override
    public void flush()
    {
        if (mask != 0x80)
        {
            onFlush(value);
            while (mask != 0)
            {
                cursor++;
                mask >>>= 1;
            }
            mask = 0x80;
            value = 0;
        }
    }

    /**
     * Closes this writer, flushing any remaining bits
     */
    @Override
    public void onClose()
    {
        if (mask != 0x80)
        {
            onWrite(value);
            value = 0;
            mask = 0;
        }
    }

    /**
     * Write the given bits
     */
    @Override
    public final void write(final long value, final int bits)
    {
        if (bits <= Long.SIZE)
        {
            // Start the mask off at the position of the first bit,
            var mask = 1L << (bits - 1);

            // then loop through the bits,
            for (var i = 0; i < bits; i++)
            {
                // get the next bit,
                final var bit = (value & mask) != 0;

                // and if writing it out causes a byte to be written out
                if (writeBit(bit))
                {
                    // then we are byte aligned, so we move to the next bit,
                    i++;
                    mask >>>= 1;

                    // compute the remaining bits,
                    var remaining = bits - i;

                    // and while there are whole bytes remaining,
                    while (remaining >= 8)
                    {
                        // decrease the remaining bits
                        remaining -= 8;

                        // move the mask and index ahead by a byte
                        i += 8;
                        mask >>>= 8;

                        // compute byte mask and use it to extract the byte value
                        final var byteMask = 0xff << remaining;
                        final int byteValue = (byte) ((value & byteMask) >>> remaining);

                        // and write the byte.
                        onWrite((byte) byteValue);
                        cursor += 8;
                    }

                    // Since we're already at the right bit index, we need to compensate
                    // for the loop increment coming up.
                    i--;
                }
                else
                {
                    // otherwise, advance the mask by one bit position.
                    mask >>>= 1;
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("Bit count must be less than or equal to " + Long.SIZE);
        }
    }

    /**
     * Writes the given bit
     *
     * @return True if a byte value was written
     */
    @Override
    public boolean writeBit(final boolean bit)
    {
        // Ensure that we haven't been closed
        assert mask != 0 : "Writer has been closed";

        // Store the bit using the mask
        value |= bit ? mask : 0;

        // If we have stored the last bit
        if (mask == 1)
        {
            // write the value
            onWrite(value);

            // and reset the mask and value
            value = 0;
            mask = 0x80;
            cursor++;
            return true;
        }
        else
        {
            // advance to the next bit
            mask >>>= 1;
            cursor++;
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFlexibleInt(final int smallBitCount, final int bigBitCount, final int value)
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

    /**
     * Flushes unwritten byte
     */
    protected abstract void onFlush(final byte value);

    /**
     * Writes the given byte value
     */
    protected abstract void onWrite(byte value);
}
