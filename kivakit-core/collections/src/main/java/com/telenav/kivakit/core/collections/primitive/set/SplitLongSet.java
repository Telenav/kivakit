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

package com.telenav.kivakit.core.collections.primitive.set;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterable;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.kivakit.core.collections.primitive.map.PrimitiveMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveSet;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A set of primitive long values. Supports typical set functions:
 * <p>
 * <b>Access</b>
 * <ul>
 *     <li>{@link #add(long)} </li>
 *     <li>{@link #contains(long)}</li>
 *     <li>{@link #remove(long)}</li>
 *     <li>{@link #clear()}</li>
 * </ul>
 * <p>
 * <b>Values</b>
 * <ul>
 *     <li>{@link #values()}</li>
 * </ul>
 * <p>
 * This class supports the {@link #hashCode()} / {@link #equals(Object)} contract and is {@link KryoSerializable}.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveSet
 * @see KryoSerializable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveSet.class)
public final class SplitLongSet extends PrimitiveSet implements LongIterable
{
    /** The child sets */
    private LongSet[] children;

    public SplitLongSet(final String objectName)
    {
        super(objectName);
    }

    protected SplitLongSet()
    {
    }

    /**
     * Stores the given value under the given value
     */
    @Override
    public boolean add(final long value)
    {
        if (set(value).add(value))
        {
            incrementSize();
            return true;
        }
        return false;
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
        initialize();
    }

    /**
     * @return True if this set contains the value
     */
    @Override
    public boolean contains(final long value)
    {
        if (isEmpty())
        {
            return false;
        }
        else
        {
            return set(value).contains(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof SplitLongSet)
        {
            final var that = (SplitLongSet) object;
            if (size() != that.size())
            {
                return false;
            }
            final var values = values();
            while (values.hasNext())
            {
                if (!that.contains(values.next()))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return values().hash();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LongIterator iterator()
    {
        return values();
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
     * @see KryoSerializable
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        children = kryo.readObject(input, LongSet[].class);
    }

    /**
     * Removes the given value from this set
     *
     * @return True if the value was removed and false if it could not be found
     */
    public boolean remove(final long value)
    {
        if (set(value).remove(value))
        {
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
                toString(values(), ", ", 10, "\n", Long::toString) + "]";
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
    }

    @Override
    protected final void copyEntries(final PrimitiveMap that, final ProgressReporter reporter)
    {
        unsupported();
    }

    @Override
    protected final PrimitiveMap newMap()
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        children = new LongSet[initialChildCountAsInt()];
    }

    /**
     * @return The submap for the given value
     */
    private LongSet set(final long value)
    {
        // Get the child index
        final var childIndex = hash(value) % children.length;

        // and if there's no child
        var child = children[childIndex];
        if (child == null)
        {
            // create one
            child = new LongSet(objectName() + ".child[" + childIndex + "]");
            child.initialSize(initialChildSizeAsInt());
            child.maximumSize(Integer.MAX_VALUE);
            child.initialize();

            // and install it in the children array.
            children[childIndex] = child;
        }
        return child;
    }
}
