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
import com.telenav.kivakit.core.collections.primitive.list.LongList;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.strings.formatting.Separators;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;

/**
 * A dynamic array of primitive long values. Supports the indexing operations in {@link LongList}. Expands the size of
 * the array if you call {@link #set(int, long)} or {@link #add(long)} and the array is not big enough.
 * <p>
 * Constructors take the same name, maximum size and estimated capacity that all {@link PrimitiveCollection}s take. In
 * addition a {@link LongArray} can construct from part or all of a primitive long[].
 * <p>
 * A sub-array can be retrieved by specifying the starting index and the length with {@link #subArray(int, int)}. The
 * sub-array is read only and will share data with the underlying parent array for efficiency.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveCollection
 * @see LongList
 * @see KryoSerializable
 * @see CompressibleCollection
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public final class LongArray extends PrimitiveArray implements LongList
{
    /**
     * Converts to and from a {@link LongArray}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<LongArray>
    {
        private final Separators separators;

        public Converter(final Listener listener, final Separators separators)
        {
            super(listener);
            this.separators = separators;
        }

        @Override
        protected LongArray onConvertToObject(final String value)
        {
            final var elements = StringList.split(value, separators.current());
            final var array = new LongArray("converted");
            array.initialize();
            for (final var element : elements)
            {
                array.add(Long.parseLong(element));
            }
            return array;
        }

        @Override
        protected String onConvertToString(final LongArray array)
        {
            final var strings = new StringList(Maximum.maximum(array.size()));
            final var values = array.iterator();
            while (values.hasNext())
            {
                strings.add(Long.toString(values.next()));
            }
            return strings.join(separators.current());
        }
    }

    /** The underlying primitive data array */
    private long[] data;

    /**
     * The index of the first element in the data array. Normally this will be zero, but for read-only sub-arrays, it
     * will be some offset into the parent array's data (which is shared).
     */
    private int offset;

    /** The index where {@link #add(long)} will add values */
    private int cursor;

    /** True if this array is a read-only sub-array of some parent array */
    private boolean isSubArray;

    public LongArray(final String objectName)
    {
        super(objectName);
    }

    protected LongArray()
    {
    }

    /**
     * Constructor for constructing read-only sub-arrays that share data with their parent.
     */
    private LongArray(final String name, final long[] data, final int offset, final int size)
    {
        this(name);

        isSubArray = true;

        this.data = data;
        this.offset = offset;
        size(size);
    }

    /**
     * Adds a value, advancing the add cursor
     */
    @Override
    public boolean add(final long value)
    {
        assert isWritable();

        if (ensureHasRoomFor(1))
        {
            set(cursor++, value);
            return true;
        }
        return false;
    }

    /**
     * This dynamic array as a primitive array
     */
    public long[] asArray()
    {
        compress(compressionMethod());
        return Arrays.copyOfRange(data, offset, offset + size());
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
        set(index, nullLong());
    }

    /**
     * Positions the add cursor
     */
    @Override
    public void cursor(final int cursor)
    {
        this.cursor = cursor;
    }

    /**
     * @return The index of the add cursor
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
        if (object instanceof LongArray)
        {
            final var that = (LongArray) object;
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
    public long get(final int index)
    {
        assert index >= 0;
        assert index < size();

        return data[offset + index];
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
        data = kryo.readObject(input, long[].class);
        offset = kryo.readObject(input, int.class);
        cursor = kryo.readObject(input, int.class);
    }

    /**
     * @return The value at the given index or the null value if the index is out of bounds
     */
    @Override
    public long safeGet(final int index)
    {
        if (index >= 0 && index < size())
        {
            return data[offset + index];
        }
        return nullLong();
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
    public void set(final int index, final long value)
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

    /**
     * Merges the given value into the value at the given index using a mask and then the bitwise "or" operator.
     */
    public void setBits(final int index, final long mask, final long value)
    {
        assert isWritable();

        // If the given index is past the end of storage,
        if (index >= data.length)
        {
            // resize the array,
            resize(PrimitiveCollection.increasedCapacity(index));
        }

        // then store the value at the given index
        data[index] = (data[index] & ~mask) | (value & mask);

        // and possibly increase the size if we've written past the end of the previous size
        size(Math.max(size(), index + 1));
    }

    @Override
    public void setPrimitive(final int index, final long value)
    {
        set(index, value);
    }

    /**
     * @return A read-only sub-array which shares underlying data with this array.
     */
    public LongArray subArray(final int index, final int size)
    {
        final var array = new LongArray(objectName(), data, offset + index, size);
        array.initialSize(0);
        array.initialize();
        array.data = data;
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(index -> Long.toString(get(index)));
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
        kryo.writeObject(output, offset);
        kryo.writeObject(output, cursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        data = newLongArray(this, "allocated");
    }

    /** Returns true if this is not a read-only sub-array */
    private boolean isWritable()
    {
        return !isSubArray;
    }

    /** Resizes this dynamic array to the given size */
    private void resize(final int size)
    {
        assert size >= size();

        // If we're writable and the size is increasing we can resize,
        if (isWritable())
        {
            // so create a new long[] of the right size,
            final var data = newLongArray(this, "resized", size);

            // copy the data from this array to the new array,
            System.arraycopy(this.data, 0, data, 0, size());

            // and assign the new long[].
            this.data = data;
        }
    }
}
