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

package com.telenav.kivakit.core.collections.primitive.array.scalars;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.PrimitiveArray;
import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.core.kernel.language.strings.StringTo;

import java.util.Arrays;

/**
 * A dynamic array of primitive byte values. Supports the indexing operations in {@link ByteList}. Expands the size of
 * the array if you call {@link #set(int, byte)} or {@link #add(byte)} and the array is not big enough.
 * <p>
 * Constructors take the same name, maximum size and estimated capacity that all {@link PrimitiveCollection}s take. In
 * addition a {@link ByteArray} can construct from part or all of a primitive byte[].
 * <p>
 * A sub-array can be retrieved by specifying the starting index and the length with {@link #sublist(int, int)}. The
 * sub-array is read only and will share data with the underlying parent array for efficiency.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveCollection
 * @see ByteList
 * @see KryoSerializable
 * @see CompressibleCollection
 * @see CompressibleCollection
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public class ByteArray extends PrimitiveArray implements ByteList
{
    /** The underlying primitive data array */
    private byte[] data;

    /** The index where {@link #add(byte)} will add values and {@link #next()} will read values */
    private int cursor;

    /** True if this array is a read-only sub-array of some parent array */
    private boolean isSubArray;

    public ByteArray(final String objectName)
    {
        super(objectName);
    }

    protected ByteArray()
    {
    }

    /**
     * Adds a value, advancing the add cursor
     */
    @Override
    public boolean add(final byte value)
    {
        assert isWritable();

        if (ensureHasRoomFor(1))
        {
            set(cursor, value);
            return true;
        }
        return false;
    }

    /**
     * Adds a long value
     *
     * @return True if the value was added
     */
    public boolean add(final long value)
    {
        assert isWritable();

        if (ensureHasRoomFor(8))
        {
            for (var shift = 7; shift >= 0; shift--)
            {
                add((byte) ((value >>> (shift * 8)) & 0xff));
            }
            return true;
        }
        return false;
    }

    /**
     * This dynamic array as a primitive array
     */
    @Override
    public byte[] asArray()
    {
        compress(Method.RESIZE);
        return Arrays.copyOfRange(data, 0, size());
    }

    /**
     * Clears this array
     */
    @Override
    public void clear()
    {
        assert isWritable();
        super.clear();
        cursor = 0;
    }

    /**
     * Sets the element at the given index to the current null value
     */
    @Override
    public void clear(final int index)
    {
        set(index, nullByte());
        cursor(index + 1);
    }

    /**
     * Positions the cursor
     */
    @Override
    public void cursor(final int cursor)
    {
        this.cursor = cursor;
    }

    /**
     * @return The index of the cursor
     */
    @Override
    public int cursor()
    {
        return cursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ByteArray)
        {
            final var that = (ByteArray) object;
            if (size() == that.size())
            {
                return iterator().identical(that.iterator());
            }
        }
        return false;
    }

    /**
     * @return The value at the given logical index.
     */
    @Override
    public byte get(final int index)
    {
        assert index >= 0;
        assert index < size();

        return data[index];
    }

    /**
     * @return The value at the given index as an unsigned value
     */
    public int getUnsigned(final int index)
    {
        return get(index) & 0xff;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return iterator().hashValue();
    }

    @Override
    public byte next()
    {
        return get(cursor++);
    }

    @Override
    public Method onCompress(final Method method)
    {
        if (size() < data.length)
        {
            resize(size());
        }

        return Method.RESIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);

        isSubArray = kryo.readObject(input, boolean.class);
        data = kryo.readObject(input, byte[].class);
        cursor = kryo.readObject(input, int.class);
    }

    /**
     * @return The value at the given index or the null value if the index is out of bounds
     */
    @Override
    public byte safeGet(final int index)
    {
        assert index >= 0;
        if (index < size())
        {
            return data[index];
        }
        return nullByte();
    }

    @Override
    public long safeGetPrimitive(final int index)
    {
        return safeGet(index);
    }

    /**
     * Sets a value at the given index, possibly extending the array size.
     */
    @Override
    public void set(final int index, final byte value)
    {
        assert isWritable();

        final var newSize = index + 1;
        final var size = size();

        // If the given index is past the end of storage,
        if (newSize > data.length)
        {
            // resize the array,
            resize(PrimitiveCollection.increasedCapacity(newSize));
        }

        // then store the value at the given index,
        data[index] = value;

        // and possibly increase the size if we've written past the end of the previous size.
        if (newSize > size)
        {
            size(newSize);
        }

        cursor(newSize);
    }

    @Override
    public void setPrimitive(final int index, final long value)
    {
        set(index, (byte) value);
    }

    /**
     * @return A read-only sub-array which shares underlying data with this array.
     */
    @Override
    public ByteArray sublist(final int offset, final int size)
    {
        final var outer = this;
        final var array = new ByteArray(objectName() + "[" + offset + " - " + (offset + size - 1) + "]")
        {
            @Override
            public byte[] asArray()
            {
                compress(Method.RESIZE);
                return Arrays.copyOfRange(outer.data, offset, size());
            }

            @Override
            public byte get(final int index)
            {
                return outer.get(offset + index);
            }

            @Override
            public byte safeGet(final int index)
            {
                return outer.safeGet(offset + index);
            }

            @Override
            public void set(final int index, final byte value)
            {
                outer.set(offset + index, value);
            }

            @Override
            public int size()
            {
                return size;
            }

            @Override
            protected void onInitialize()
            {
            }
        };
        array.initialize();
        return array;
    }

    public String toBinaryString()
    {
        return toString(", ", 10, "\n", index -> StringTo.binary(getUnsigned(index), 8), 49);
    }

    public String toHexString()
    {
        return toString(", ", 10, "\n", (index) -> String.format("%02x", getUnsigned(index)), 49);
    }

    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", cursor = " + cursor() + ", size = " + size() + "]\n" +
                toHexString() + "\n" + toBinaryString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, isSubArray);
        kryo.writeObject(output, data);
        kryo.writeObject(output, cursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        data = newByteArray(this, "allocated");
    }

    /** Returns true if this is not a read-only sub-array */
    private boolean isWritable()
    {
        return !isSubArray;
    }

    /** Resizes this dynamic array's capacity to the given size */
    private void resize(final int size)
    {
        assert size >= size();

        // If we're writable and the size is increasing we can resize,
        if (isWritable())
        {
            // so create a new byte[] of the right size,
            final var data = newByteArray(this, "resized", size);

            // copy the data from this array to the new array,
            System.arraycopy(this.data, 0, data, 0, size());

            // and assign the new byte[].
            this.data = data;
        }
    }
}
