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

import com.telenav.kivakit.core.collections.primitive.ShortCollection;
import com.telenav.kivakit.core.collections.primitive.iteration.ShortIterable;
import com.telenav.kivakit.core.collections.primitive.iteration.ShortIterator;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveList;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A collection of primitive short values. All primitive collections have a name that can be retrieved with {@link
 * #objectName()} and a size retrieved with {@link Sized#size()} and they can be emptied with {@link #clear()}. Values
 * in this collection can be iterated with an {@link ShortIterator} returned by the {@link ShortIterable#iterator()}
 * method.
 * <p>
 * New values can be added with {@link #add(short)}, {@link #addAll(ShortCollection)} and {@link #addAll(short[])} and
 * removed with {@link #remove(short)}. In addition, {@link Quantizable} values can be added with {@link #addAll(List)},
 * where each value is quantized via {@link Quantizable#quantum()} before being added.
 * <p>
 * Whether a given value or collection of values is in the collection can be determined with {@link #contains(short)}
 * and {@link #containsAll(ShortCollection)}.
 *
 * @author jonathanl (shibo)
 * @see Named
 * @see Sized
 * @see ShortIterable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveList.class)
public interface ShortList extends ShortCollection, PrimitiveList
{
    /**
     * Binary search adapted from Java Arrays#binarySearch
     *
     * @param target The target to search for
     * @return The index of the given target. The return value will be &lt; 0 if the target was not found.
     */
    default int binarySearch(final short target)
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
     * Sets the value at the given index to the {@link #nullShort()} value
     */
    default void clear(final int index)
    {
        set(index, nullShort());
    }

    /**
     * @return The first element in this list
     */
    default short first()
    {
        return get(0);
    }

    /**
     * @return The short at the given index
     */
    short get(int index);

    /**
     * {@inheritDoc}
     */
    @Override
    default long getPrimitive(final int index)
    {
        return get(index);
    }

    @Override
    default boolean isPrimitiveNull(final long value)
    {
        return isNull((short) value);
    }

    /***
     * @return Default iterator implementation
     */
    @Override
    default ShortIterator iterator()
    {
        return new ShortIterator()
        {
            int index;

            @Override
            public boolean hasNext()
            {
                return index < size();
            }

            @Override
            public short next()
            {
                short next;
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
    default short last()
    {
        return get(size() - 1);
    }

    /**
     * @return The value at the given index, but if the index is out of range, null is returned.
     */
    short safeGet(int index);

    /**
     * Sets the list entry at the given index to the given value
     */
    void set(int index, short value);
}
