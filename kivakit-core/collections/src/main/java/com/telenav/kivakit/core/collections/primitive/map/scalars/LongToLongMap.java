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
import com.telenav.kivakit.core.collections.primitive.map.PrimitiveMap;
import com.telenav.kivakit.core.collections.primitive.map.PrimitiveScalarMap;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

import java.util.Arrays;

/**
 * A map from long keys to long values. Supports typical map functions:
 * <p>
 * <b>Access</b>
 * <ul>
 *     <li>{@link #get(long)} </li>
 *     <li>{@link #put(long, long)}</li>
 *     <li>{@link #remove(long)}</li>
 *     <li>{@link #increment(long)}</li>
 *     <li>{@link #clear()}</li>
 * </ul>
 * <p>
 * <b>Keys and Values</b>
 * <ul>
 *     <li>{@link #keys()}</li>
 *     <li>{@link #values()}</li>
 *     <li>{@link #entries(EntryVisitor)} </li>
 *     <li>{@link #containsKey(long)}</li>
 * </ul>
 * <p>
 * This class supports the {@link #hashCode()} / {@link #equals(Object)} contract and is {@link KryoSerializable}.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveMap
 * @see KryoSerializable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public final class LongToLongMap extends PrimitiveMap implements PrimitiveScalarMap
{
    /**
     * Interface for visiting map entries with {@link #entries(EntryVisitor)}
     */
    public interface EntryVisitor
    {
        void onEntry(long key, long value);
    }

    /** The keys */
    private long[] keys;

    /** The values */
    private long[] values;

    public LongToLongMap(final String objectName)
    {
        super(objectName);
    }

    protected LongToLongMap()
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
    public boolean containsKey(final long key)
    {
        return contains(keys, key);
    }

    /**
     * Calls the visitor with each key / value pair in the map
     */
    public void entries(final EntryVisitor visitor)
    {
        final var indexes = nonEmptyIndexes(keys);
        while (indexes.hasNext())
        {
            final var index = indexes.next();
            visitor.onEntry(keys[index], values[index]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof LongToLongMap)
        {
            final var that = (LongToLongMap) object;
            if (this == that)
            {
                return true;
            }
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
     * @return The value for the given key. The returned value should be checked with {@link #isNull(long)} to determine
     * if it represents null.
     */
    public long get(final long key)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            final var index = Arrays.binarySearch(keys, key);
            return index < 0 ? nullLong() : values[index];
        }
        else
        {
            return values[index(keys, key)];
        }
    }

    @Override
    public long getScalar(final long key)
    {
        return get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return keys().hash() ^ values().hash();
    }

    /**
     * Increments the value for the given key
     */
    public void increment(final long key)
    {
        assert compressionMethod() != Method.FREEZE;

        put(key, get(key) + 1);
    }

    @Override
    public boolean isScalarKeyNull(final long key)
    {
        return isNull(key);
    }

    @Override
    public boolean isScalarValueNull(final long value)
    {
        return isNull(value);
    }

    /**
     * @return The keys in this map in an undefined order
     */
    public LongIterator keys()
    {
        return nonEmptyValues(keys);
    }

    /**
     * Freezes this primitive collection into an optimal memory representation. Once frozen, the collection can no
     * longer be modified.
     */
    @Override
    public Method onCompress(final Method method)
    {
        if (method == Method.RESIZE)
        {
            return super.onCompress(method);
        }
        else
        {
            final var frozenKeys = newLongArray(this, "froze", size());
            final var frozenValues = newLongArray(this, "froze", size());
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
     * Stores the given value under the given key. The value may not be null. To remove a value, call {@link
     * #remove(long)}.
     *
     * @return True if a new value was added, false if an existing value was overwritten
     */
    public boolean put(final long key, final long value)
    {
        assert !isEmpty(value);
        assert compressionMethod() != Method.FREEZE;

        final var keys = this.keys;
        final var values = this.values;

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

    @Override
    public void putScalar(final long key, final long value)
    {
        put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        keys = kryo.readObject(input, long[].class);
        values = kryo.readObject(input, long[].class);
    }

    /**
     * Removes the given key from the map along with its value
     *
     * @return True if the key was removed, false if it was not found
     */
    public boolean remove(final long key)
    {
        assert compressionMethod() != Method.FREEZE;

        // Get index of key
        final var index = index(keys, key);

        // If the key was found,
        if (!isEmpty(keys[index]))
        {
            // remove it
            keys[index] = TOMBSTONE_INT;
            values[index] = nullLong();
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
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(keys(), values(), (key, value) -> key + " -> " + value);
    }

    /**
     * @return The values in this map in an undefined order
     */
    public LongIterator values()
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

        final var that = (LongToLongMap) uncast;
        keys = that.keys;
        values = that.values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyEntries(final PrimitiveMap uncast, final ProgressReporter reporter)
    {
        final var that = (LongToLongMap) uncast;
        final var indexes = nonEmptyIndexes(that.keys);
        while (indexes.hasNext())
        {
            final var index = indexes.next();
            final var key = that.keys[index];
            final var value = that.values[index];
            if (!isNull(key) && !isNull(value))
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
    protected LongToLongMap newMap()
    {
        return new LongToLongMap(objectName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        keys = newLongArray(this, "allocated");
        values = newLongArray(this, "allocated");
    }

    @Override
    protected int slots()
    {
        return keys.length;
    }
}
