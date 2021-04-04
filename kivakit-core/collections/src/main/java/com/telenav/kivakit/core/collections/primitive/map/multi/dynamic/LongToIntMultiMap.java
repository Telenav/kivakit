////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map.multi.dynamic;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.list.PrimitiveList;
import com.telenav.kivakit.core.collections.primitive.list.store.IntLinkedListStore;
import com.telenav.kivakit.core.collections.primitive.map.multi.IntMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveScalarMultiMap;
import com.telenav.kivakit.core.collections.primitive.array.scalars.IntArray;
import com.telenav.kivakit.core.collections.primitive.iteration.IntIterator;
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
public final class LongToIntMultiMap extends PrimitiveMultiMap implements IntMultiMap, PrimitiveScalarMultiMap
{
    /** The array of arrays */
    private IntLinkedListStore values;

    /** Map from key to lists index */
    private SplitLongToIntMap indexes;

    public LongToIntMultiMap(final String objectName)
    {
        super(objectName);
    }

    protected LongToIntMultiMap()
    {
    }

    /**
     * Adds the given value to the list for the given key
     */
    public void add(final long key, final int value)
    {
        if (ensureHasRoomFor(1))
        {
            final var indexes = this.indexes;
            var index = indexes.get(key);
            final var head = indexes.isNull(index) ? IntLinkedListStore.NEW_LIST : index;
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
    public IntArray get(final long key)
    {
        final var array = new IntArray("get");
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
    public IntIterator iterator(final long key)
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
    public void putAll(final long key, final int[] values)
    {
        if (ensureHasRoomFor(1))
        {
            final var indexes = this.indexes;
            var index = indexes.get(key);
            final var head = indexes.isNull(index) ? IntLinkedListStore.NEW_LIST : index;
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
            final var head = indexes.isNull(index) ? IntLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    /**
     * Adds all the given values to the list for the given key
     */
    @Override
    public void putAll(final long key, final IntArray values)
    {
        if (ensureHasRoomFor(1))
        {
            final var indexes = this.indexes;
            var index = indexes.get(key);
            final var head = indexes.isNull(index) ? IntLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    @Override
    public void putPrimitiveList(final long key, final PrimitiveList values)
    {
        putAll(key, (IntArray) values);
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

        values = kryo.readObject(input, IntLinkedListStore.class);
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

        values = new IntLinkedListStore(objectName() + ".values");
        indexes.initialSize(initialSize());
        values.initialize();
    }
}
