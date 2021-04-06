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

package com.telenav.kivakit.core.collections.primitive.list;

import com.telenav.kivakit.core.collections.primitive.ByteCollection;
import com.telenav.kivakit.core.collections.primitive.iteration.ByteIterator;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveList;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;

/**
 * A byte collection supporting indexed operations. The first and last values in the list can be retrieved with {@link
 * #first()} and {@link #last()}. Values at a given index can be altered and retrieved with {@link #clear(int)}, {@link
 * #get(int)}, {@link #safeGet(int)} and {@link #set(int, byte)}.
 * <p>
 * If the list is sorted, it can be searched with {@link #binarySearch(byte)}, which returns the index of the value if
 * it is found and a value less than zero if it is not.
 * <p>
 * A default iterator implementation is provided by {@link #iterator()}.
 *
 * @author jonathanl (shibo)
 * @see ByteCollection
 */
@UmlClassDiagram(diagram = DiagramPrimitiveList.class)
public interface ByteList extends ByteCollection, PrimitiveList, CompressibleCollection
{
    /**
     * @return This byte list as an array (subclasses may provide a more efficient implementation)
     */
    default byte[] asArray()
    {
        final var array = new byte[size()];
        for (var index = 0; index < size(); index++)
        {
            array[index] = get(index);
        }
        return array;
    }

    /**
     * Binary search adapted from Java Arrays#binarySearch
     *
     * @param target The target to search for
     * @return The index of the given target. The return value will be &lt; 0 if the target was not found.
     */
    default int binarySearch(final byte target)
    {
        var low = 0;
        var high = size() - 1;

        while (low <= high)
        {
            final var middle = (low + high) / 2;
            final var value = get(middle);

            if (value < target)
            {
                low = middle + 1;
            }
            else if (value > target)
            {
                high = middle - 1;
            }
            else
            {
                return middle; // target found
            }
        }
        return -(low + 1);  // target not found.
    }

    /**
     * Sets the value at the given index to the {@link #nullByte()} value
     */
    default void clear(final int index)
    {
        set(index, nullByte());
    }

    /**
     * @return Location of the read / write cursor
     */
    int cursor();

    /**
     * Sets the position of the read / write cursor
     */
    void cursor(int position);

    /**
     * @return The first element in this list
     */
    default byte first()
    {
        return get(0);
    }

    /**
     * @return The byte at the given index
     */
    byte get(int index);

    /**
     * {@inheritDoc}
     */
    @Override
    default long getPrimitive(final int index)
    {
        return get(index);
    }

    /**
     * @return True if the cursor is not yet at the end of values
     */
    default boolean hasNext()
    {
        return cursor() < size();
    }

    @Override
    default boolean isPrimitiveNull(final long value)
    {
        return isNull((byte) value);
    }

    /***
     * @return Default iterator implementation
     */
    @Override
    default ByteIterator iterator()
    {
        return new ByteIterator()
        {
            int index;

            @Override
            public boolean hasNext()
            {
                return index < size();
            }

            @Override
            public byte next()
            {
                byte next;
                do
                {
                    next = safeGet(index++);
                }
                while (isNull(next) && index < size());
                return next;
            }
        };
    }

    /**
     * @return The last element in this list
     */
    default byte last()
    {
        return get(size() - 1);
    }

    /**
     * @return The next byte from the read cursor
     */
    byte next();

    default byte read()
    {
        return next();
    }

    default boolean[] readBooleans(final int size)
    {
        final var values = new boolean[size];
        var mask = 0;
        var value = 0;
        for (var i = 0; i < size; i++)
        {
            if (mask == 0)
            {
                mask = 0x80;
                value = next();
            }
            if ((value & mask) != 0)
            {
                values[i] = true;
            }
            mask >>>= 1;
        }
        return values;
    }

    /**
     * @return An int value read in the format written by {@link #writeFlexibleChar(char)}
     */
    default char readFlexibleChar()
    {
        final var value = (char) readUnsigned();
        if (value < 255)
        {
            return value;
        }
        final var high = readUnsigned();
        final var low = readUnsigned();
        return (char) (high << 8 | low);
    }

    /**
     * @return An int value read in the format written by {@link #writeFlexibleShort(short)}
     */
    default short readFlexibleShort()
    {
        final var value = (short) readUnsigned();
        if (value < 255)
        {
            return value;
        }
        final var high = readUnsigned();
        final var low = readUnsigned();
        return (short) (high << 8 | low);
    }

    default int readInt()
    {
        return (readUnsigned() << 24)
                | (readUnsigned() << 16)
                | (readUnsigned() << 8)
                | readUnsigned();
    }

    /**
     * @return The next byte masked to an unsigned value in an int
     */
    default int readUnsigned()
    {
        return next() & 0xff;
    }

    /**
     * Resets the read / write cursor to position zero
     */
    default void reset()
    {
        cursor(0);
    }

    /**
     * @return The value at the given index, but if the index is out of range, null is returned.
     */
    byte safeGet(int index);

    /**
     * Sets the list entry at the given index to the given value. The cursor moves to the position after the specified
     * index after the value is written.
     */
    void set(int index, byte value);

    /**
     * @return A sub-section of this byte list
     */
    ByteList sublist(final int offset, final int size);

    /**
     * Writes the given value at the current cursor location and moves the cursor forward by one
     */
    default void write(final byte value)
    {
        set(cursor(), value);
    }

    default void writeBooleans(final boolean[] values)
    {
        var current = 0;
        var mask = 0x80;
        for (final var value : values)
        {
            if (mask == 0)
            {
                mask = 0x80;
            }
            if (value)
            {
                current |= mask;
            }
            mask >>>= 1;
            if (mask == 0)
            {
                write((byte) current);
                current = 0;
            }
        }
        if (mask != 0)
        {
            write((byte) current);
        }
    }

    /**
     * Writes the given char value as either one byte (if it's less than 255) or three bytes (if it's greater than or
     * equal to 255). Useful when the vast majority of values will be small but there might occasionally be a value
     * larger than one byte.
     */
    default void writeFlexibleChar(final char value)
    {
        if (value < 255)
        {
            write((byte) value);
        }
        else
        {
            write((byte) 255);
            final var high = value >>> 8;
            final var low = value & 0xff;
            write((byte) high);
            write((byte) low);
        }
    }

    /**
     * Writes the given int value as either one byte (if it's less than 255) or three bytes (if it's greater than or
     * equal to 255). Useful when the vast majority of values will be small but there might occasionally be a value
     * larger than one byte.
     */
    default void writeFlexibleShort(final short value)
    {
        if (value < 255)
        {
            write((byte) value);
        }
        else
        {
            write((byte) 255);
            final var high = value >>> 8;
            final var low = value & 0xff;
            write((byte) high);
            write((byte) low);
        }
    }

    default void writeInt(final int value)
    {
        write((byte) ((value >>> 24) & 0xff));
        write((byte) ((value >>> 16) & 0xff));
        write((byte) ((value >>> 8) & 0xff));
        write((byte) ((value) & 0xff));
    }
}
