////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map.multi.fixed;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import org.junit.Test;

public class LongToLongFixedMultiMapTest extends CoreCollectionsUnitTest
{
    @Test
    public void test()
    {
        final var map = new LongToByteFixedMultiMap("map");
        map.initialize();
        final var bytes = new ByteArray("bytes");
        bytes.initialize();
        bytes.add((byte) 1);
        bytes.add((byte) 2);
        bytes.add((byte) 3);
        map.putAll(1L, bytes);
        map.putAll(2L, bytes);
        map.putAll(3L, bytes);
        ensureEqual(bytes, map.get(1L));
        ensureEqual(bytes, map.get(2L));
        ensureEqual(bytes, map.get(3L));
    }
}
