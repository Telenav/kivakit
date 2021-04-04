////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map.split;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.map.PrimitiveScalarMap;
import com.telenav.kivakit.core.collections.primitive.map.SplitPrimitiveMap;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.collections.primitive.map.scalars.LongToLongMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

/**
 * A map from long keys to long values. Supports typical map functions:
 * <p>
 * <b>Access</b>
 * <ul>
 *     <li>{@link #get(long)} </li>
 *     <li>{@link #put(long, long)}</li>
 *     <li>{@link #remove(long)}</li>
 *     <li>{@link #clear()}</li>
 * </ul>
 * <p>
 * <b>Keys and Values</b>
 * <ul>
 *     <li>{@link #keys()}</li>
 *     <li>{@link #values()}</li>
 *     <li>{@link #entries(LongToLongMap.EntryVisitor)} </li>
 *     <li>{@link #containsKey(long)}</li>
 * </ul>
 * <p>
 * This class supports the {@link #hashCode()} / {@link #equals(Object)} contract and is {@link KryoSerializable}.
 *
 * @author jonathanl (shibo)
 * @see SplitPrimitiveMap
 * @see KryoSerializable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public final class SplitLongToLongMap extends SplitPrimitiveMap implements PrimitiveScalarMap
{
    private LongToLongMap[] children;

    private int size;

    public SplitLongToLongMap(final String objectName)
    {
        super(objectName);
    }

    protected SplitLongToLongMap()
    {
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
        size = 0;
    }

    /**
     * @return True if this map contains the given key
     */
    public boolean containsKey(final long key)
    {
        final var child = child(key, false);
        return child != null && child.containsKey(key);
    }

    /**
     * Calls the visitor with each key / value pair in the map
     */
    public void entries(final LongToLongMap.EntryVisitor visitor)
    {
        for (final var child : children)
        {
            if (child != null)
            {
                child.entries(visitor);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof SplitLongToLongMap)
        {
            final var that = (SplitLongToLongMap) object;
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
     * @return The value for the given key
     */
    public long get(final long key)
    {
        final var child = child(key, false);
        return child == null ? nullLong() : child.get(key);
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
        final var outer = this;
        return new LongIterator()
        {
            private int childIndex;

            private LongIterator keys;

            @Override
            public boolean hasNext()
            {
                if (keys != null && keys.hasNext())
                {
                    return true;
                }
                keys = null;
                while (keys == null && childIndex < outer.children.length)
                {
                    final var next = outer.children[childIndex++];
                    if (next != null && !next.isEmpty())
                    {
                        keys = next.keys();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public long next()
            {
                return keys.next();
            }
        };
    }

    @Override
    public Method onCompress(final Method method)
    {
        for (final var child : children)
        {
            if (child != null)
            {
                child.compress(method);
            }
        }
        return method;
    }

    /**
     * Stores the given value under the given key
     */
    public void put(final long key, final long value)
    {
        assert compressionMethod() != Method.FREEZE;

        if (child(key, true).put(key, value))
        {
            size++;
        }
    }

    @Override
    public void putScalar(final long key, final long value)
    {
        put(key, value);
    }

    /**
     * @see KryoSerializable
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);

        children = kryo.readObject(input, LongToLongMap[].class);
        size = kryo.readObject(input, int.class);
    }

    /**
     * Removes the given key from the map along with its value
     *
     * @return True if the key was removed, false if it was not found
     */
    public boolean remove(final long key)
    {
        assert compressionMethod() != Method.FREEZE;
        final var child = child(key, false);
        if (child != null)
        {
            if (child.remove(key))
            {
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return size;
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
        final var outer = this;
        return new LongIterator()
        {
            private int childIndex;

            private LongIterator values;

            @Override
            public boolean hasNext()
            {
                if (values != null && values.hasNext())
                {
                    return true;
                }
                values = null;
                while (values == null && childIndex < outer.children.length)
                {
                    final var next = outer.children[childIndex++];
                    if (next != null && !next.isEmpty())
                    {
                        values = next.values();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public long next()
            {
                return values.next();
            }
        };
    }

    /**
     * @see KryoSerializable
     */
    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, children);
        kryo.writeObject(output, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        children = new LongToLongMap[initialChildCountAsInt()];
    }

    /**
     * @return Gets the child map for the given key. If there is no map, one is created if create is true.
     */
    private LongToLongMap child(final long key, final boolean create)
    {
        // Get the child index from the key
        final var childIndex = hash(key) % children.length;

        // and if the child index is null and we should create a new child,
        var child = children[childIndex];
        if (child == null && create)
        {
            // then allocate and configure the child
            child = new LongToLongMap(objectName() + ".child[" + childIndex + "]");
            child.copyConfiguration(this);
            child.initialSize(initialChildSize());
            child.maximumSize(Integer.MAX_VALUE);
            child.initialize();

            // and assign it to the children array.
            children[childIndex] = child;
        }

        return child;
    }
}
