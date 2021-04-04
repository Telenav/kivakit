////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map.multi.dynamic;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import org.junit.Test;

public class LongToLongMultiMapTest extends CoreCollectionsUnitTest
{
    @Test
    public void test()
    {
        final var map = new LongToLongMultiMap("test");
        map.initialize();
        for (long i = 0; i < 100; i++)
        {
            for (long j = 0; j < 10; j++)
            {
                map.add(i, j);
            }
        }
        for (long i = 0; i < 100; i++)
        {
            final var values = map.get(i);
            for (long j = 0; j < 10; j++)
            {
                // values are added to the list in reverse order
                ensureEqual(j, values.get((int) (9 - j)));
            }
            final var iterator = map.iterator(i);
            if (iterator != null)
            {
                long expected = 9;
                while (iterator.hasNext())
                {
                    final var value = iterator.next();
                    ensureEqual(expected--, value);
                }
            }
        }
    }
}
