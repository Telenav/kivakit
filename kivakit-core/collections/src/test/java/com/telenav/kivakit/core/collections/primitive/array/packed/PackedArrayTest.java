////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.packed;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;
import org.junit.Test;

public class PackedArrayTest extends CoreCollectionsUnitTest
{
    @Test
    public void testAccess()
    {
        final var values = new PackedArray("test");
        values.bits(BitCount._3, PackedPrimitiveArray.OverflowHandling.NO_OVERFLOW);
        values.initialize();

        values.set(0, 3L);
        ensureEqual(1, values.size());
        ensureEqual(3L, values.get(0));
        values.set(1, 5L);
        ensureEqual(2, values.size());
        ensureEqual(5L, values.get(1));
        values.set(21, 7L);
        ensureEqual(22, values.size());
        ensureEqual(7L, values.get(21));
        values.set(22, 7L);
        ensureEqual(23, values.size());
        ensureEqual(7L, values.get(22));
    }

    @Test
    public void testBig()
    {
        final var values = new PackedArray("test");
        values.bits(BitCount._34, PackedPrimitiveArray.OverflowHandling.NO_OVERFLOW);
        values.initialize();
        for (var i = 0; i < 100; i++)
        {
            values.set(i, 17179869183L);
            ensureEqual(17179869183L, values.get(i));
        }
    }

    @Test
    public void testExhaustive()
    {
        // Test each bit length
        for (var bits = 1; bits <= Long.SIZE; bits++)
        {
            trace("Testing " + bits + " bit array");

            // Create packed array with given bit length
            final var count = BitCount.bitCount(bits);

            final var values = new PackedArray("test");
            values.initialSize(32);
            values.bits(count, PackedPrimitiveArray.OverflowHandling.NO_OVERFLOW);
            values.initialize();

            // Maximum value is (2 ^ bits) - 1
            final var maximum = bits >= 63 ? Long.MAX_VALUE : (1L << bits) - 1;

            // Loop through all indexes
            for (var i = 0; i < values.initialSizeAsInt(); i++)
            {
                // and test storage of a range of values
                final var iterations = 32;
                final var step = Math.max(1, (maximum / iterations) + 1);
                for (var value = 0L; value < maximum && value >= 0; value += step)
                {
                    final Long left = values.safeGet(i - 1);
                    final Long right = values.safeGet(i + 1);
                    values.set(i, value);
                    ensureEqual(value, values.get(i));
                    ensureEqual(left, values.safeGet(i - 1));
                    ensureEqual(right, values.safeGet(i + 1));
                }
            }
        }

        testAccess();
    }

    @Test
    public void testNoOverflow()
    {
        final var values = new PackedArray("test");
        values.bits(BitCount._3, PackedPrimitiveArray.OverflowHandling.NO_OVERFLOW);
        values.hasNullLong(false);
        values.initialize();

        values.set(0, 99L);
        ensureEqual(values.get(0), 7L);
    }

    @Test
    public void testNull()
    {
        final var values = new PackedArray("test");
        values.bits(BitCount._3, PackedPrimitiveArray.OverflowHandling.NO_OVERFLOW);
        values.nullLong(7);
        values.initialize();

        values.set(0, 3L);
        ensure(!values.isNull(values.get(0)));

        values.set(1, 5L);
        ensure(!values.isNull(values.get(1)));

        values.set(21, values.nullLong());
        ensure(values.isNull(values.get(21)));

        values.set(22, values.nullLong());
        ensure(values.isNull(values.get(22)));

        for (var i = 2; i <= 22; i++)
        {
            ensureEqual(values.nullLong(), values.get(i));
        }
    }

    @Test
    public void testOverflow()
    {
        final var values = new PackedArray("test");
        values.bits(BitCount._3, PackedPrimitiveArray.OverflowHandling.ALLOW_OVERFLOW);
        values.hasNullLong(false);
        values.initialize();

        values.set(0, 15);
        ensureEqual(values.get(0), 7L);
        values.set(0, 16);
        ensureEqual(values.get(0), 0L);
    }

    @Test
    public void testSerialization()
    {
        if (!isQuickTest())
        {
            final var values = new PackedArray("test");
            values.initialSize(100);
            values.bits(BitCount._3, PackedPrimitiveArray.OverflowHandling.NO_OVERFLOW);
            values.initialize();
            for (var i = 0; i < 7; i++)
            {
                values.set(i, i);
            }
            serializationTest(values);
        }
    }
}
