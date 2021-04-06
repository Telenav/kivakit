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

package com.telenav.kivakit.core.collections.primitive.map.multi.dynamic;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.list.PrimitiveList;
import com.telenav.kivakit.core.collections.primitive.list.store.LongLinkedListStore;
import com.telenav.kivakit.core.collections.primitive.map.multi.LongMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveScalarMultiMap;
import com.telenav.kivakit.core.collections.primitive.array.scalars.LongArray;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.collections.primitive.map.split.SplitLongToIntMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A map from long -&gt; list of longs.
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public final class LongToLongMultiMap extends PrimitiveMultiMap implements LongMultiMap, PrimitiveScalarMultiMap
{
    /** The array of arrays */
    private LongLinkedListStore values;

    /** Map from key to lists index */
    private SplitLongToIntMap indexes;

    public LongToLongMultiMap(final String objectName)
    {
        super(objectName);
    }

    protected LongToLongMultiMap()
    {
    }

    /**
     * Adds the given value to the list for the given key
     */
    public void add(final long key, final long value)
    {
        if (ensureHasRoomFor(1))
        {
            final var indexes = this.indexes;
            var index = indexes.get(key);
            final var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = values.add(head, value);
            indexes.put(key, index);
        }
    }

    /**
     * @return True if this map contains a list for the given key
     */
    public boolean containsKey(final long key)
    {
        final var indexes = this.indexes;
        return !indexes.isNull(indexes.get(key));
    }

    /**
     * @return An array of longs for the given key. This method is convenient in some cases, but it is less efficient
     * than {@link #iterator(long)}.
     */
    @Override
    public LongArray get(final long key)
    {
        final var array = new LongArray("get");
        array.initialSize(initialChildSizeAsInt());
        array.initialize();

        final var iterator = iterator(key);
        if (iterator != null)
        {
            while (iterator.hasNext())
            {
                array.add(iterator.next());
            }
        }
        return array;
    }

    @Override
    public PrimitiveList getPrimitiveList(final long key)
    {
        return get(key);
    }

    @Override
    public boolean isScalarKeyNull(final long key)
    {
        return isNull(key);
    }

    /**
     * @return An iterator over the list of longs for the given key
     */
    public LongIterator iterator(final long key)
    {
        final var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            return values.list(index);
        }
        return null;
    }

    /**
     * @return An {@link Iterable} over the keys in this map
     */
    public LongIterator keys()
    {
        return indexes.keys();
    }

    @Override
    public Method onCompress(final Method method)
    {
        if (method == Method.RESIZE)
        {
            return super.onCompress(method);
        }
        else
        {
            indexes.compress(method);
            values.compress(method);

            return method;
        }
    }

    /**
     * Adds all the given values to the list for the given key
     */
    public void putAll(final long key, final long[] values)
    {
        if (ensureHasRoomFor(1))
        {
            final var indexes = this.indexes;
            var index = indexes.get(key);
            final var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    @Override
    public void putAll(final long key, final List<? extends Quantizable> values)
    {
        if (ensureHasRoomFor(1))
        {
            final var indexes = this.indexes;
            var index = indexes.get(key);
            final var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    /**
     * Adds all the given values to the list for the given key
     */
    @Override
    public void putAll(final long key, final LongArray values)
    {
        if (ensureHasRoomFor(1))
        {
            final var indexes = this.indexes;
            var index = indexes.get(key);
            final var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    @Override
    public void putPrimitiveList(final long key, final PrimitiveList values)
    {
        putAll(key, (LongArray) values);
    }

    @Override
    public void putPrimitiveList(final long key, final List<? extends Quantizable> values)
    {
        putAll(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);

        values = kryo.readObject(input, LongLinkedListStore.class);
        indexes = kryo.readObject(input, SplitLongToIntMap.class);
    }

    /**
     * @return The number of entries in this map
     */
    @Override
    public int size()
    {
        return indexes.size();
    }

    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(keys(), key ->
                        {
                            final var list = get(key);
                            return list.iterator();
                        },
                        (key, values) -> values == null ? "null" : key + " -> " + values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, values);
        kryo.writeObject(output, indexes);
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        indexes = new SplitLongToIntMap(objectName() + ".indexes");
        indexes.initialSize(initialSize());
        indexes.initialize();

        values = new LongLinkedListStore(objectName() + ".values");
        indexes.initialSize(initialSize());
        values.initialize();
    }
}
