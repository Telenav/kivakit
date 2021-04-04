////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.list.store;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitIntArray;
import com.telenav.kivakit.core.collections.primitive.array.strings.PackedStringArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveList;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

/**
 * A store of string values that can be written with {@link #set(int, String)} and read with {@link #get(int)}.
 * <p>
 * This class is {@link KryoSerializable}.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveCollection
 */
@UmlClassDiagram(diagram = DiagramPrimitiveList.class)
public class PackedStringStore extends PrimitiveCollection
{
    private PackedStringArray strings;

    private SplitIntArray indexes;

    public PackedStringStore(final String objectName)
    {
        super(objectName);
    }

    protected PackedStringStore()
    {
    }

    @Override
    public Count capacity()
    {
        return strings.capacity().plus(indexes.capacity());
    }

    public String get(final int index)
    {
        final var list = indexes.safeGet(index);
        if (!indexes.isNull(list))
        {
            return strings.safeGet(list);
        }
        return null;
    }

    @Override
    public Method onCompress(final Method method)
    {
        strings.compress(method);
        indexes.compress(method);
        return Method.RESIZE;
    }

    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);

        strings = kryo.readObject(input, PackedStringArray.class);
        indexes = kryo.readObject(input, SplitIntArray.class);
    }

    public void set(final int index, final String value)
    {
        assert index > 0;
        if (value != null)
        {
            indexes.set(index, strings.add(value));
        }
        else
        {
            indexes.set(index, nullInt());
        }
    }

    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, strings);
        kryo.writeObject(output, indexes);
    }

    @Override
    protected void onInitialize()
    {
        super.onInitialize();
        indexes = new SplitIntArray(objectName() + ".indexes");
        indexes.initialize();

        strings = new PackedStringArray(objectName() + ".strings");
        strings.initialize();
    }
}
