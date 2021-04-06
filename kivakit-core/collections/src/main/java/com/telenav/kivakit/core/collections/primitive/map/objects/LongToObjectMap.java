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

package com.telenav.kivakit.core.collections.primitive.map.objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.map.PrimitiveMap;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A map from primitive long to object.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public final class LongToObjectMap<T> extends PrimitiveMap
{
    /** The keys */
    private long[] keys;

    /** The values */
    private T[] values;

    public LongToObjectMap(final String objectName)
    {
        super(objectName);
    }

    protected LongToObjectMap()
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

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof LongToObjectMap)
        {
            if (object == this)
            {
                return true;
            }
            final var that = (LongToObjectMap<T>) object;
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
     * if it represents a null value.
     */
    public T get(final long key)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            final var index = Arrays.binarySearch(keys, key);
            return index < 0 ? null : values[index];
        }
        else
        {
            return values[index(keys, key)];
        }
    }

    @Override
    public int hashCode()
    {
        return keys().hash() ^ values().hashCode();
    }

    /**
     * @return The keys in this map
     */
    public LongIterator keys()
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
            final var frozenKeys = newLongArray(this, "froze", size());
            final T[] frozenValues = newObjectArray(this, "froze", size());
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
        }
        return method;
    }

    /**
     * Stores the given value under the given key
     *
     * @return True if a new value was added, false if an existing value was overwritten
     */
    public boolean put(final long key, final T value)
    {
        assert compressionMethod() != Method.FREEZE;

        assert value != null;

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

    @SuppressWarnings("unchecked")
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        keys = kryo.readObject(input, long[].class);
        values = (T[]) kryo.readClassAndObject(input);
    }

    public void remove(final long key)
    {
        assert compressionMethod() != Method.FREEZE;

        // Get index of key
        final var index = index(keys, key);

        // If the key was found,
        if (!isNull(keys[index]))
        {
            // remove it
            keys[index] = TOMBSTONE_LONG;
            values[index] = null;
            decreaseSize(1);
        }
    }

    @Override
    public String toString()
    {
        return "[LongToObjectMap name = " + objectName() + ", size = " + size() + "]";
    }

    public Iterator<T> values()
    {
        return nonEmptyValues(values);
    }

    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);
        kryo.writeObject(output, keys);
        kryo.writeClassAndObject(output, values);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void copy(final PrimitiveMap uncast)
    {
        super.copy(uncast);
        final var that = (LongToObjectMap<T>) uncast;
        keys = that.keys;
        values = that.values;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void copyEntries(final PrimitiveMap uncast, final ProgressReporter reporter)
    {
        final var that = (LongToObjectMap<T>) uncast;
        final var indexes = nonEmptyIndexes(that.keys);
        while (indexes.hasNext())
        {
            final var index = indexes.next();
            final var key = that.keys[index];
            final var value = that.values[index];
            if (!isNull(key) && value != null)
            {
                put(key, value);
            }
            reporter.next();
        }
    }

    @Override
    protected PrimitiveMap newMap()
    {
        return new LongToObjectMap<T>(objectName());
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        keys = newLongArray(this, "allocated");
        values = newObjectArray(this, "allocated");
    }

    @Override
    protected int slots()
    {
        return keys.length;
    }
}
