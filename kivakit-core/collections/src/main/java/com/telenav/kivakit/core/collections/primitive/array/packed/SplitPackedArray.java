////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.packed;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.PrimitiveSplitArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitIntArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitLongArray;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.collections.primitive.list.LongList;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveSplitArray;
import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.language.objects.Objects;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * {@link SplitPackedArray} maintains an array of packed arrays, indexing them as a single larger array. The purpose in
 * this is to avoid resizing as a very large packed array grows. Instead, when a write index is greater than the size of
 * the {@link SplitPackedArray}, a new {@link PackedArray} is allocated and added to the list. This design is similar to
 * the "rope" pattern for handling very large strings.
 *
 * @author jonathanl (shibo)
 * @see LongList
 * @see PrimitiveSplitArray
 * @see SplitIntArray
 * @see SplitByteArray
 * @see SplitLongArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveSplitArray.class)
public final class SplitPackedArray extends PrimitiveSplitArray implements LongList, PackedPrimitiveArray
{
    // Bit count of packed arrays
    private BitCount bits;

    // PrimitiveArray of packed arrays
    private PackedArray[] children;

    // True if unused elements should be initialized to the null value
    private boolean initializeElements;

    private OverflowHandling overflow;

    private int childSize;

    /** The index at which adding takes place */
    private int cursor;

    public SplitPackedArray(final String objectName)
    {
        super(objectName);
    }

    protected SplitPackedArray()
    {
    }

    /**
     * Adds a value, advancing the add cursor
     */
    @Override
    public boolean add(final long value)
    {
        assert ensureHasRoomFor(1);
        set(cursor++, value);
        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public SplitPackedArray bits(final BitCount bits, final OverflowHandling overflow)
    {
        ensure(bits != null);
        ensure(!bits.isZero());
        ensure(!bits.isGreaterThan(Count._64));
        this.bits = bits;
        this.overflow = overflow;
        return this;
    }

    @Override
    public BitCount bits()
    {
        return bits;
    }

    @Override
    public Count capacity()
    {
        var capacity = 0;
        for (final var child : children)
        {
            if (child != null)
            {
                capacity += child.capacity().asInt();
            }
        }
        return Count.count(capacity);
    }

    @Override
    public void clear()
    {
        super.clear();
        unsupported();
    }

    @Override
    public void copyConfiguration(final PrimitiveCollection primitiveCollection)
    {
        super.copyConfiguration(primitiveCollection);

        final var that = (PackedArray) primitiveCollection;
        bits(that.bits(), that.overflow());
    }

    public void copyTo(final SplitPackedArray that)
    {
        for (var index = 0; index < size(); index++)
        {
            that.set(index, get(index));
        }
    }

    @Override
    public int cursor()
    {
        return cursor;
    }

    @Override
    public void cursor(final int cursor)
    {
        this.cursor = cursor;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof SplitPackedArray)
        {
            final var that = (SplitPackedArray) object;
            return Objects.equalPairs(children, that.children, bits, that.bits);
        }
        return false;
    }

    @Override
    public long get(final int index)
    {
        assert index >= 0 : "Index " + index + " must be >= 0";
        assert index < size() : "Index " + index + " must be < " + size();

        // Get the value from the array for the given index. Note that we have to safe get from the
        // sub-arrays here because they might not be fully populated
        final var childIndex = index / childSize;
        if (childIndex < children.length)
        {
            final var child = children[childIndex];
            if (child != null)
            {
                return child.safeGet(index % childSize);
            }
        }
        return childArray(childIndex).safeGet(index % childSize);
    }

    public boolean getBoolean(final int index)
    {
        final var value = safeGet(index);
        return value != 0;
    }

    public Count getCount(final int index)
    {
        final var count = get(index);
        return isNull(count) ? null : Count.count(count);
    }

    public long getSigned(final int index)
    {
        final var childIndex = index / childSize;
        if (childIndex < children.length)
        {
            final var child = children[childIndex];
            if (child != null)
            {
                return child.getSigned(index % childSize);
            }
        }
        return childArray(childIndex).getSigned(index % childSize);
    }

    @Override
    public int hashCode()
    {
        return Hash.many(Arrays.hashCode(children), bits);
    }

    public void initializeElements(final boolean initializeElements)
    {
        this.initializeElements = initializeElements;
    }

    @Override
    public LongIterator iterator()
    {
        return new LongIterator()
        {
            final int index = 0;

            @Override
            public boolean hasNext()
            {
                return index < size();
            }

            @Override
            public long next()
            {
                return get(index);
            }
        };
    }

    @Override
    public SplitPackedArray nullLong(final long nullLong)
    {
        if (hasNullLong())
        {
            ensure(Count.count(nullLong).bitsToRepresent().isLessThanOrEqualTo(bits),
                    "Bits required to represent " + nullLong + " is too large for array of " + bits + " bits");
        }
        super.nullLong(nullLong);
        return this;
    }

    @Override
    public Method onCompress(final Method method)
    {
        for (final var array : children)
        {
            if (array != null)
            {
                array.compress(method);
            }
        }
        return Method.RESIZE;
    }

    @Override
    public OverflowHandling overflow()
    {
        return overflow;
    }

    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        childSize = initialChildSizeAsInt();
        children = kryo.readObject(input, PackedArray[].class);
        bits = kryo.readObject(input, BitCount.class);
        initializeElements = kryo.readObject(input, boolean.class);
    }

    /**
     * @return The value at the given index or null if the index is out of bounds or the value at the given index is the
     * null value
     */
    @Override
    public long safeGet(final int index)
    {
        final var childIndex = index / childSize;
        if (childIndex < children.length)
        {
            final var child = children[childIndex];
            if (child != null)
            {
                return child.safeGet(index % childSize);
            }
        }
        return childArray(childIndex).safeGet(index % childSize);
    }

    /**
     * @return The value at the given index or null if the index is out of bounds or the value at the given index is the
     * null value
     */
    public boolean safeGetBoolean(final int index)
    {
        final var value = safeGet(index);
        return value != 0;
    }

    public Count safeGetCount(final int index)
    {
        final var count = safeGetInt(index);
        return isNull(count) || count < 0 ? null : Count.count(count);
    }

    public int safeGetInt(final int index)
    {
        return (int) safeGet(index);
    }

    @Override
    public long safeGetPrimitive(final int index)
    {
        return safeGet(index);
    }

    @Override
    public void set(final int index, final long value)
    {
        assert index >= 0 : "Index " + index + " must be >= 0";

        // Set the value in the array for the given index
        final int childIndex = index / childSize;
        childArray(childIndex).set(index % childSize, value);

        // Initialize elements and increase the size if we've written past the end
        if (index >= size())
        {
            if (initializeElements && nullLong() != 0)
            {
                for (var i = size(); i < index; i++)
                {
                    set(i, nullLong());
                }
            }
            size(index + 1);
        }

        assert index < size();
        assert isValueStoredAtIndex(index, value) : "Failed to store " + value + " in " + bits
                + " bit array at index " + index;
    }

    public void setBoolean(final int index, final boolean value)
    {
        set(index, value ? 1L : 0L);
    }

    public void setCount(final int index, final Count count)
    {
        set(index, count == null ? nullInt() : count.get());
    }

    public void setInt(final int index, final int value)
    {
        set(index, value);
    }

    @Override
    public void setPrimitive(final int index, final long value)
    {
        set(index, value);
    }

    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + ", bits = " + bits + ", children = " + children.length + "]\n" +
                toString(index -> Long.toString(get(index)));
    }

    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);
        kryo.writeObject(output, children);
        kryo.writeObject(output, bits);
        kryo.writeObject(output, initializeElements);
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        childSize = initialChildSizeAsInt();
        children = new PackedArray[initialChildCountAsInt()];
    }

    private PackedArray childArray(final int childIndex)
    {
        if (childIndex >= children.length)
        {
            children = Arrays.copyOf(children, childIndex * 2);
        }
        var array = children[childIndex];
        if (array == null)
        {
            array = new PackedArray(objectName() + ".child[" + childIndex + "]");
            array.copyConfiguration(this);
            array.initialSize(initialChildSize());
            array.maximumSize(maximumChildSize());
            array.initialize();

            children[childIndex] = array;
        }
        return array;
    }

    private boolean isValueStoredAtIndex(final int index, final long value)
    {
        if (value < 0)
        {
            return getSigned(index) == value;
        }
        return get(index) == value;
    }
}
