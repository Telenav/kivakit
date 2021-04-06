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

package com.telenav.kivakit.core.collections.primitive.array.packed;

import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.PrimitiveSplitArray;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link BigSplitPackedArray} maintains an array of packed arrays, indexing them as a single larger array. The purpose
 * in this is to avoid resizing as a very large packed array grows. Instead, when a write index is greater than the size
 * of the {@link BigSplitPackedArray}, a new {@link PackedArray} is allocated and added to the list. This design is
 * similar to the "rope" pattern for handling very large strings.
 *
 * @author jonathanl (shibo)
 */
public class BigSplitPackedArray extends PrimitiveSplitArray implements PackedPrimitiveArray
{
    private List<PackedArray> arrays;

    private BitCount bits;

    private OverflowHandling overflow;

    private long size;

    public BigSplitPackedArray(final String objectName)
    {
        super(objectName);
    }

    @Override
    public BitCount bits()
    {
        return bits;
    }

    @Override
    public BigSplitPackedArray bits(final BitCount bits, final OverflowHandling overflow)
    {
        this.bits = bits;
        this.overflow = overflow;
        return this;
    }

    @Override
    public void copyConfiguration(final PrimitiveCollection that)
    {
        super.copyConfiguration(that);

        bits = ((PackedArray) that).bits();
    }

    public Boolean getBoolean(final long index)
    {
        final var value = getLong(index);
        if (value != null)
        {
            return value != 0;
        }
        return null;
    }

    public Count getCount(final long index)
    {
        final var count = getInteger(index);
        return count == null ? null : Count.count(count);
    }

    public int getInt(final long index)
    {
        return getLong(index).intValue();
    }

    public Integer getInteger(final long index)
    {
        final var value = getLong(index);
        if (value != null)
        {
            return value.intValue();
        }
        return null;
    }

    public Long getLong(final long index)
    {
        // If our index is negative,
        if (index < 0)
        {
            // throw an exception
            throw new IndexOutOfBoundsException("Index " + index + " < 0");
        }

        // If our index is beyond the size,
        else if (index >= size)
        {
            // throw an exception
            throw new IndexOutOfBoundsException("Index " + index + " >= " + size);
        }
        else
        {
            // Get the value from the array for the given index
            return array(index).get((int) (index % initialChildSizeAsInt()));
        }
    }

    public LongIterator indexesOfNonNullValues()
    {
        final var outer = this;
        return new LongIterator()
        {
            private int whichArray;

            private PackedArray array;

            private int index;

            private long next;

            @SuppressWarnings({ "ConstantConditions" })
            @Override
            public boolean hasNext()
            {
                do
                {
                    // If we don't have an array to look through
                    if (array == null)
                    {
                        // find any next one
                        while (whichArray < outer.size())
                        {
                            final var array = outer.arrays.get(whichArray++);
                            if (array != null)
                            {
                                this.array = array;
                                index = 0;
                                break;
                            }
                        }
                    }

                    // If there's an array to look through
                    if (array != null)
                    {
                        // look through the array
                        while (index < array.size())
                        {
                            final Long value = array.safeGet(index++);
                            if (value != null)
                            {
                                next = ((long) (whichArray - 1) * outer.initialChildSizeAsInt()) + (index - 1);
                                return true;
                            }
                        }

                        // we're done looking through this array
                        array = null;
                    }
                }
                while (whichArray < outer.arrays.size());

                return false;
            }

            @Override
            public long next()
            {
                return next;
            }
        };
    }

    public long longSize()
    {
        return size;
    }

    @Override
    public Method onCompress(final Method method)
    {
        for (final var array : arrays)
        {
            array.compress(method);
        }

        return Method.RESIZE;
    }

    @Override
    public OverflowHandling overflow()
    {
        return overflow;
    }

    /**
     * @return The value at the given index or null if the index is out of bounds or the value at the given index is the
     * null value
     */
    public boolean safeGetBoolean(final long index)
    {
        return safeGetLong(index) != 0;
    }

    public Count safeGetCount(final long index)
    {
        final var count = safeGetInt(index);
        return count == -1 ? null : Count.count(count);
    }

    /**
     * @return The value at the given index or null if the index is out of bounds or the value at the given index is the
     * null value
     */
    public int safeGetInt(final long index)
    {
        return (int) safeGetLong(index);
    }

    /**
     * @return The value at the given index or null if the index is out of bounds or the value at the given index is the
     * null value
     */
    public long safeGetLong(final long index)
    {
        if (index < 0 || index >= longSize())
        {
            return -1;
        }
        return array(index).safeGet((int) (index % initialChildSizeAsInt()));
    }

    public void setBoolean(final long index, final Boolean value)
    {
        setLong(index, value == null ? null : (value ? 1L : 0L));
    }

    public void setCount(final long index, final Count count)
    {
        setInteger(index, count == null ? null : count.asInt());
    }

    public void setInt(final long index, final int value)
    {
        setLong(index, (long) value);
    }

    public void setInteger(final long index, final Integer value)
    {
        setLong(index, value == null ? null : (long) value);
    }

    public void setLong(final long index, final Long value)
    {
        // If our index is negative,
        if (index < 0)
        {
            // throw an exception
            throw new IndexOutOfBoundsException("Index " + index + " < 0");
        }
        else
        {
            // Set the value in the array for the given index
            array(index).set((int) (index % initialChildSizeAsInt()), value);

            // Possibly increase the size if we've written past the end of the previous size
            size = Math.max(size, index + 1);
        }
    }

    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(index -> Long.toString(getLong(index)));
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        super.onInitialize();
        arrays = new ArrayList<>(2048);
    }

    private PackedArray array(final long index)
    {
        final var arrayIndex = (int) (index / initialChildSizeAsInt());
        var array = arrays.get(arrayIndex);
        if (array == null)
        {
            array = new PackedArray(objectName() + ".child[" + arrayIndex + "]");
            array.copyConfiguration(this);
            array.bits(bits, OverflowHandling.NO_OVERFLOW);
            array.nullLong(nullLong());
            array.hasNullLong(hasNullLong());
            array.maximumSize(maximumChildSize());
            array.initialSize(initialChildSize());
            arrays.set(arrayIndex, array);
        }
        return array;
    }
}
