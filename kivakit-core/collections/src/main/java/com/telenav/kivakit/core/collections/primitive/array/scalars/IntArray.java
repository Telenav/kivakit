////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.scalars;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.PrimitiveArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.kivakit.core.collections.primitive.list.IntList;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.strings.formatting.Separators;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.util.Arrays;

/**
 * A dynamic array of primitive int values. Supports the indexing operations in {@link IntList}. Expands the size of the
 * array if you call {@link #set(int, int)} or {@link #add(int)} and the array is not big enough.
 * <p>
 * Constructors take the same name, maximum size and estimated capacity that all {@link PrimitiveCollection}s take. In
 * addition a {@link IntArray} can construct from part or all of a primitive int[].
 * <p>
 * A sub-array can be retrieved by specifying the starting index and the length with {@link #subArray(int, int)}. The
 * sub-array is read only and will share data with the underlying parent array for efficiency.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveCollection
 * @see IntList
 * @see KryoSerializable
 * @see CompressibleCollection
 * @see CompressibleCollection
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public final class IntArray extends PrimitiveArray implements IntList
{
    public static class Converter extends BaseStringConverter<IntArray>
    {
        private final Separators separators;

        public Converter(final Listener listener, final Separators separators)
        {
            super(listener);
            this.separators = separators;
        }

        @Override
        protected IntArray onConvertToObject(final String value)
        {
            final var elements = StringList.split(value, separators.current());
            final var array = new IntArray("converted");
            array.initialize();
            for (final var element : elements)
            {
                array.add(Integer.parseInt(element));
            }
            return array;
        }

        @Override
        protected String onConvertToString(final IntArray array)
        {
            final var strings = new StringList(Maximum.maximum(array.size()));
            final var values = array.iterator();
            while (values.hasNext())
            {
                strings.add(Integer.toString(values.next()));
            }
            return strings.join(separators.current());
        }
    }

    /** The underlying primitive data array */
    private int[] data;

    /**
     * The index of the first element in the data array. Normally this will be zero, but for read-only sub-arrays, it
     * will be some offset into the parent array's data (which is shared).
     */
    private int offset;

    /** The index where {@link #add(int)} will add values */
    private int cursor;

    /** True if this array is a read-only sub-array of some parent array */
    private boolean isSubArray;

    public IntArray(final String objectName)
    {
        super(objectName);
    }

    protected IntArray()
    {
    }

    /**
     * Constructor for constructing read-only sub-arrays that share data with their parent.
     */
    private IntArray(final String name, final int[] data, final int offset, final int size)
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
    public boolean add(final int value)
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
    public int[] asArray()
    {
        compress(Method.RESIZE);
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
        set(index, nullInt());
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
        if (object instanceof IntArray)
        {
            final var that = (IntArray) object;
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
    public int get(final int index)
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
        data = kryo.readObject(input, int[].class);
        offset = kryo.readObject(input, int.class);
        cursor = kryo.readObject(input, int.class);
    }

    /**
     * @return The value at the given index or the null value if the index is out of bounds
     */
    @Override
    public int safeGet(final int index)
    {
        assert index >= 0;
        if (index < size())
        {
            return data[offset + index];
        }
        return nullInt();
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
    public void set(final int index, final int value)
    {
        assert isWritable();

        final var newSize = index + 1;
        final var size = size();

        // If the given index is past the end of storage,
        if (newSize > data.length)
        {
            // resize the array,
            resize(increasedCapacity(newSize));
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
        set(index, (int) value);
    }

    /**
     * @return A read-only sub-array which shares underlying data with this array.
     */
    public IntArray subArray(final int index, final int size)
    {
        final var array = new IntArray(objectName(), data, offset + index, size);
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
        data = newIntArray(this, "allocated");
    }

    /** Returns true if this is not a read-only sub-array */
    private boolean isWritable()
    {
        return !isSubArray;
    }

    /** Resizes this dynamic array to the given size */
    private void resize(final int size)
    {
        // If we're writable and the size is increasing we can resize,
        if (isWritable())
        {
            // so create a new int[] of the right size,
            final var data = newIntArray(this, "resized", size);

            // copy the data from this array to the new array,
            System.arraycopy(this.data, 0, data, 0, size());

            // and assign the new int[].
            this.data = data;
        }
    }
}
