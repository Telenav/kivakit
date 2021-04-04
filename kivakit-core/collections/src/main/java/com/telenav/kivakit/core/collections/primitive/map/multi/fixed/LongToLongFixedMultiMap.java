////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map.multi.fixed;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.list.PrimitiveList;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveScalarMultiMap;
import com.telenav.kivakit.core.collections.primitive.array.scalars.LongArray;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.collections.primitive.map.scalars.LongToIntMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A compact multi-map which allows one-time put of a fixed list of values. Adding more values is not supported.
 *
 * @author jonathanl (shibo)
 * @see LongArray
 * @see PrimitiveMultiMap
 * @see KryoSerializable
 */
@SuppressWarnings({ "ConstantConditions" })
@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public final class LongToLongFixedMultiMap extends PrimitiveMultiMap implements PrimitiveScalarMultiMap
{
    private static final long TERMINATOR = Long.MAX_VALUE - 1;

    /** Null terminated lists of values */
    private LongArray values;

    /** Map from key to values index */
    private LongToIntMap indexes;

    public LongToLongFixedMultiMap(final String objectName)
    {
        super(objectName);
    }

    protected LongToLongFixedMultiMap()
    {
    }

    /**
     * @return True if this map contains the given key
     */
    public boolean containsKey(final long key)
    {
        return !indexes.isNull(indexes.get(key));
    }

    /**
     * @return An long array for the given key
     */
    public LongArray get(final long key)
    {
        final var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            final var values = new LongArray("get");
            values.initialSize(initialChildSizeAsInt());
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                final var value = this.values.get(i);
                if (value == TERMINATOR)
                {
                    return values;
                }
                values.add(value);
            }
            return values;
        }
        return null;
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
     * @return An iterator over the keys in this map
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
     * Puts the given values under the given key
     */
    public void putAll(final long key, final long[] values)
    {
        // If we haven't already put a value for this key
        assert indexes != null;
        assert isNull(indexes.get(key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            final var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put(key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(TERMINATOR);
        }
    }

    /**
     * Puts the given values under the given key
     */
    public void putAll(final long key, final LongArray values)
    {
        // If we haven't already put a value for this key
        assert indexes != null;
        assert isNull(indexes.get(key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            final var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put(key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(TERMINATOR);
        }
    }

    public void putAll(final long key, final List<? extends Quantizable> values)
    {
        // If we haven't already put a value for this key
        assert isNull(indexes.get((int) key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            final var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put((int) key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(TERMINATOR);
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

        values = kryo.readObject(input, LongArray.class);
        indexes = kryo.readObject(input, LongToIntMap.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return indexes.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(keys(), key -> get(key) == null ? null : get(key).iterator(),
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        indexes = new LongToIntMap(objectName() + ".indexes");
        indexes.initialSize(initialSize());
        indexes.initialize();

        values = new LongArray(objectName() + ".values");
        indexes.initialSize(initialSize());
        values.initialize();

        // Add a value in the first index spot because index 0 is invalid
        values.add(nullLong());
    }
}
