////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.arrays;

import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import org.junit.Test;

public class ByteArrayArrayTest extends CoreCollectionsUnitTest
{
    @Test
    public void test()
    {
        final var store = new ByteArrayArray("test");
        store.initialize();

        final var a = bytes(10, 20, 30, 40);
        final var aIndex = store.add(a);

        final var b = bytes(2, 3, 5, 7, 11);
        final var bIndex = store.add(b);

        ensureEqual(a, store.get(aIndex));
        ensureEqual(b, store.get(bIndex));
        ensureEqual(4, store.length(aIndex));
        ensureEqual(5, store.length(bIndex));
    }

    private ByteArray bytes(final int... values)
    {
        final var array = new ByteArray("test");
        array.initialize();
        for (final int value : values)
        {
            array.add((byte) value);
        }
        return array;
    }
}
