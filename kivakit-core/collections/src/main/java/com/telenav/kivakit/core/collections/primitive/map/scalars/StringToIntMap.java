////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map.scalars;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.iteration.IntIterator;
import com.telenav.kivakit.core.collections.primitive.map.PrimitiveMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Iterator;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A map from String keys to int values. Supports typical map functions:
 * <p>
 * <b>Access</b>
 * <ul>
 *     <li>{@link #get(String)} </li>
 *     <li>{@link #put(String, int)}</li>
 *     <li>{@link #remove(String)}</li>
 *     <li>{@link #clear()}</li>
 * </ul>
 * <p>
 * <b>Keys and Values</b>
 * <ul>
 *     <li>{@link #keys()}</li>
 *     <li>{@link #values()}</li>
 * </ul>
 * <p>
 * This class supports the {@link #hashCode()} / {@link #equals(Object)} contract and is {@link KryoSerializable}.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveMap
 * @see KryoSerializable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public final class StringToIntMap extends PrimitiveMap
{
    /** The keys */
    private String[] keys;

    /** The values */
    private int[] values;

    public StringToIntMap(final String objectName)
    {
        super(objectName);
    }

    protected StringToIntMap()
    {
    }

    @Override
    public Count capacity()
    {
        return Count.count(keys.length);
    }

    /**
     * Clears all key/value pairs from this map
     */
    @Override
    public void clear()
    {
        super.clear();
        clear(keys);
    }

    /**
     * @return True if this map contains the given key
     */
    public boolean containsKey(final String key)
    {
        return contains(keys, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof StringToIntMap)
        {
            if (object == this)
            {
                return true;
            }
            final var that = (StringToIntMap) object;
            if (size() != that.size())
            {
                return false;
            }
            final var keys = keys();
            while (keys.hasNext())
            {
                final var key = keys.next();
                final var value = get(key);
                if (value != that.get(key))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return The value for the given key. The returned value should be checked with {@link #isNull(int)} to determine
     * if it represents a null value.
     */
    public int get(final String key)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            final var index = Arrays.binarySearch(keys, key);
            return index < 0 ? nullInt() : values[index];
        }
        else
        {
            return values[index(keys, key)];
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return keys().hashCode() ^ values().hash();
    }

    /**
     * @return The keys in this map
     */
    public Iterator<String> keys()
    {
        return nonEmptyValues(keys);
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
            final var frozenKeys = newStringArray(this, "froze", size());
            final var frozenValues = newIntArray(this, "froze", size());
            final var keys = keys();
            for (var i = 0; keys.hasNext(); i++)
            {
                frozenKeys[i] = keys.next();
            }
            Arrays.sort(frozenKeys);
            for (var i = 0; i < frozenValues.length; i++)
            {
                frozenValues[i] = get(frozenKeys[i]);
            }
            this.keys = frozenKeys;
            values = frozenValues;
            return method;
        }
    }

    /**
     * Stores the given value under the given key
     */
    public boolean put(final String key, final int value)
    {
        assert compressionMethod() != Method.FREEZE;

        ensure(!isEmpty(value));

        // Get the index to put at
        final var index = index(keys, key);

        // If the slot at the given index is empty
        if (isEmpty(keys[index]))
        {
            // then we're adding a new key/value pair
            keys[index] = key;
            values[index] = value;
            increaseSize();
            return true;
        }
        else
        {
            // otherwise, we're just changing the value
            values[index] = value;
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        keys = kryo.readObject(input, String[].class);
        values = kryo.readObject(input, int[].class);
    }

    /**
     * Removes the key value pair for the given key
     *
     * @return True if removal succeeded, false if the key was not found
     */
    public boolean remove(final String key)
    {
        assert compressionMethod() != Method.FREEZE;

        // Get index of key
        final var index = index(keys, key);

        // If the key was found,
        if (!isEmpty(keys[index]))
        {
            // remove it
            keys[index] = TOMBSTONE_STRING;
            values[index] = nullInt();
            decreaseSize(1);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[StringToObjectMap name = " + objectName() + ", size = " + size() + "]";
    }

    /**
     * @return Iterator over the values in this map
     */
    public IntIterator values()
    {
        return nonEmptyValues(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, keys);
        kryo.writeObject(output, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copy(final PrimitiveMap uncast)
    {
        super.copy(uncast);

        final var that = (StringToIntMap) uncast;
        keys = that.keys;
        values = that.values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyEntries(final PrimitiveMap uncast, final ProgressReporter reporter)
    {
        final var that = (StringToIntMap) uncast;
        final var indexes = nonEmptyIndexes(that.keys);
        while (indexes.hasNext())
        {
            final var index = indexes.next();
            final var key = that.keys[index];
            final var value = that.values[index];
            if (key != null && !isNull(value))
            {
                put(key, value);
            }
            reporter.next();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PrimitiveMap newMap()
    {
        return new StringToIntMap(objectName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        keys = newStringArray(objectName() + ".keys", "allocated");
        values = newIntArray(objectName() + ".values", "allocated");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int slots()
    {
        return keys.length;
    }
}
