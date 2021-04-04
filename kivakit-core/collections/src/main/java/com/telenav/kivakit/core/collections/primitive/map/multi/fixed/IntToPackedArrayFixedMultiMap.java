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
import com.telenav.kivakit.core.collections.primitive.array.packed.PackedPrimitiveArray;
import com.telenav.kivakit.core.collections.primitive.array.packed.SplitPackedArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.IntArray;
import com.telenav.kivakit.core.collections.primitive.list.PrimitiveList;
import com.telenav.kivakit.core.collections.primitive.map.multi.LongMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.PrimitiveScalarMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.scalars.IntToIntMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.kivakit.core.collections.primitive.array.scalars.LongArray;
import com.telenav.kivakit.core.collections.primitive.iteration.IntIterator;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;

import java.util.List;

/**
 * A compact multi-map which allows one-time put of a fixed list of values. Adding more values is not supported.
 *
 * @author jonathanl (shibo)
 * @see IntArray
 * @see PrimitiveMultiMap
 * @see KryoSerializable
 */
@SuppressWarnings({ "ConstantConditions" })
@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public final class IntToPackedArrayFixedMultiMap extends PrimitiveMultiMap implements LongMultiMap, PrimitiveScalarMultiMap
{
    /** Null terminated lists of values */
    private SplitPackedArray values;

    /** Map from key to values index */
    private IntToIntMap indexes;

    private BitCount bits;

    private PackedPrimitiveArray.OverflowHandling overflow;

    private long listTerminator;

    public IntToPackedArrayFixedMultiMap(final String objectName)
    {
        super(objectName);
    }

    protected IntToPackedArrayFixedMultiMap()
    {
    }

    public IntToPackedArrayFixedMultiMap bits(final BitCount bits, final PackedPrimitiveArray.OverflowHandling overflow)
    {
        this.bits = bits;
        this.overflow = overflow;
        return this;
    }

    /**
     * @return True if this map contains the given key
     */
    public boolean containsKey(final int key)
    {
        return !indexes.isNull(indexes.get(key));
    }

    /**
     * @return A long array for the given key
     */
    public LongArray get(final int key)
    {
        final var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            final var values = new LongArray("get");
            values.initialSize(256);
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                final var value = this.values.get(i);
                if (value == listTerminator)
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
    public LongArray get(final long key)
    {
        return get((int) key);
    }

    @Override
    public PrimitiveList getPrimitiveList(final long key)
    {
        return get((int) key);
    }

    /**
     * @return A long array for the given key
     */
    public LongArray getSigned(final int key)
    {
        final var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            final var values = new LongArray("get");
            values.initialSize(256);
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                final var value = this.values.getSigned(i);
                if (value == listTerminator)
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
    public PrimitiveList getSignedPrimitiveList(final long key)
    {
        final var index = indexes.get((int) key);
        if (!indexes.isNull(index))
        {
            final var values = new LongArray("get");
            values.initialSize(256);
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                final var value = this.values.getSigned(i);
                if (value == listTerminator)
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
    public boolean isScalarKeyNull(final long key)
    {
        return isNull((int) key);
    }

    /**
     * @return An iterator over the keys in this map
     */
    public IntIterator keys()
    {
        return indexes.keys();
    }

    public IntToPackedArrayFixedMultiMap listTerminator(final long listTerminator)
    {
        this.listTerminator = listTerminator;
        return this;
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

            return Method.MIXED;
        }
    }

    @Override
    public void putAll(final long key, final LongArray values)
    {
        putAll((int) key, values);
    }

    @Override
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
            this.values.add(listTerminator);
        }
    }

    /**
     * Puts the given values under the given key
     */
    public void putAll(final int key, final long[] values)
    {
        // If we haven't already put a value for this key
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
            this.values.add(listTerminator);
        }
    }

    /**
     * Puts the given values under the given key
     */
    public void putAll(final int key, final LongArray values)
    {
        // If we haven't already put a value for this key
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
            this.values.add(listTerminator);
        }
    }

    @Override
    public void putPrimitiveList(final long key, final PrimitiveList values)
    {
        putAll((int) key, (LongArray) values);
    }

    @Override
    public void putPrimitiveList(final long key, final List<? extends Quantizable> values)
    {
        putAll((int) key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);

        bits = kryo.readObject(input, BitCount.class);
        overflow = kryo.readObject(input, PackedPrimitiveArray.OverflowHandling.class);
        listTerminator = kryo.readObject(input, long.class);
        values = kryo.readObject(input, SplitPackedArray.class);
        indexes = kryo.readObject(input, IntToIntMap.class);
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
                toString(keys(), key -> get(key.intValue()) == null ? null : get(key.intValue()).iterator(),
                        (key, values) -> values == null ? "null" : key + " -> " + values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, bits);
        kryo.writeObject(output, overflow);
        kryo.writeObject(output, listTerminator);
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

        indexes = new IntToIntMap(objectName() + ".indexes");
        indexes.initialSize(initialSize());
        indexes.initialize();

        values = new SplitPackedArray(objectName() + ".values");
        values.bits(bits, overflow);
        values.initialSize(initialSize());
        values.initialize();

        // Add a value in the first index spot because index 0 is invalid
        values.add(nullLong());
    }
}
