////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.scalars;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.array.PrimitiveSplitArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveSplitArray;
import com.telenav.kivakit.core.collections.primitive.IntCollection;
import com.telenav.kivakit.core.collections.primitive.list.IntList;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

import java.util.Arrays;

/**
 * A split primitive array of int values. A split array has one key (an index) so it is one-dimensional, although it has
 * an array of children each of which is a primitive array. This is a sort of sparse array that tends to perform well
 * when values cluster by index (which they tend to do with map data identifiers). In addition, allocation of child
 * arrays is quick and memory efficient versus trying to manage one very large array. This design also works around the
 * 2GB limitation of Java arrays (which can only be indexed by int values).
 * <p>
 * Supports the operations of {@link IntCollection}, with the exception of {@link #clear()}. Indexing operations in
 * {@link IntList} are supported just as in {@link IntArray}, but the values are distributed across an array of child
 * {@link IntArray} objects.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveSplitArray
 * @see IntList
 * @see IntArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveSplitArray.class)
public final class SplitIntArray extends PrimitiveSplitArray implements IntList
{
    /** The child arrays */
    private IntArray[] children;

    /** The index at which adding takes place */
    private int cursor;

    private int childSize;

    public SplitIntArray(final String objectName)
    {
        super(objectName);
    }

    protected SplitIntArray()
    {
    }

    /**
     * Adds a value, advancing the add cursor
     */
    @Override
    public boolean add(final int value)
    {
        assert ensureHasRoomFor(1);
        set(cursor++, value);
        return true;
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
    public int cursor()
    {
        return cursor;
    }

    @Override
    public void cursor(final int cursor)
    {
        this.cursor = cursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof SplitIntArray)
        {
            final var that = (SplitIntArray) object;
            return size() == that.size() && iterator().identical(that.iterator());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int get(final int index)
    {
        final var childIndex = index / childSize;
        if (childIndex < children.length)
        {
            final var child = children[childIndex];
            if (child != null)
            {
                return child.get(index % childSize);
            }
        }
        return childArray(childIndex).get(index % childSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return iterator().hashValue();
    }

    @Override
    public Method onCompress(final Method method)
    {
        // Go through our children,
        for (final var child : children)
        {
            // and if the child is not null (this is a sparse array)
            if (child != null)
            {
                // then trim the child array.
                child.compress(method);
            }
        }

        return Method.RESIZE;
    }

    /**
     * @see KryoSerializable
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        children = kryo.readObject(input, IntArray[].class);
        childSize = initialChildSizeAsInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int safeGet(final int index)
    {
        final var childIndex = index / childSize;
        if (childIndex < children.length)
        {
            final var child = children[childIndex];
            if (child != null)
            {
                return child.safeGet(index % childSize);
            }
        }
        return childArray(childIndex).safeGet(index % childSize);
    }

    @Override
    public long safeGetPrimitive(final int index)
    {
        return safeGet(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final int index, final int value)
    {
        // Set the value into the array for the index
        final int childIndex = index / childSize;
        childArray(childIndex).set(index % childSize, value);

        // then increase the size if we wrote past the end.
        final var size = index + 1;
        if (size > size())
        {
            size(size);
        }
    }

    @Override
    public void setPrimitive(final int index, final long value)
    {
        set(index, (int) value);
    }

    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(index -> Long.toString(get(index)));
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
    protected void onInitialize()
    {
        super.onInitialize();
        childSize = initialChildSizeAsInt();
        children = new IntArray[initialChildCountAsInt()];
    }

    /**
     * @return The child array for the given index
     */
    private IntArray childArray(final int childIndex)
    {
        // If the child index is beyond the length of the children array,
        if (childIndex >= children.length)
        {
            // resize the children array to double the size,
            children = Arrays.copyOf(children, childIndex * 2);
        }

        // then get the child array,
        var array = children[childIndex];

        // and if it's null,
        if (array == null)
        {
            // create a new child
            array = new IntArray(objectName() + ".child[" + childIndex + "]");
            array.copyConfiguration(this);
            array.initialSize(childSize);
            array.maximumSize(maximumChildSizeAsInt());
            array.initialize();

            // and add it to the children array.w
            children[childIndex] = array;
        }

        return array;
    }
}
