////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.scalars;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.mutable.MutableInteger;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.core.test.UnitTest.Repeats.ALLOW_REPEATS;
import static com.telenav.kivakit.core.test.UnitTest.Repeats.NO_REPEATS;

public class IntArrayTest extends CoreCollectionsUnitTest
{
    @Test
    public void testAdd()
    {
        final var array = array();
        array.hasNullInt(false);
        final var values = randomIntList(ALLOW_REPEATS);
        values.forEach(array::add);
        resetIndex();
        values.forEach(value -> ensureEqual(array.get(nextIndex()), value));
    }

    @Before
    public void testBefore()
    {
        iterations(5_000);
    }

    @Test
    public void testClear()
    {
        final var array = array();
        array.hasNullInt(false);
        randomInts(NO_REPEATS, array::add);
        randomIndexes(NO_REPEATS, index ->
        {
            ensure(!array.isNull(array.get(index)));
            array.clear(index);
            ensure(!array.isNull(array.get(index)));
        });

        array.nullInt(-1);
        randomInts(NO_REPEATS, value ->
        {
            if (!array.isNull(value))
            {
                array.add(value);
            }
        });
        randomIndexes(ALLOW_REPEATS, index ->
        {
            if (index < array.size())
            {
                array.set(index, 99);
                ensure(!array.isNull(array.get(index)));
                array.clear(index);
                ensure(array.isNull(array.get(index)));
            }
        });
    }

    @Test
    public void testEqualsHashCode()
    {
        final var map = new HashMap<IntArray, Integer>();
        loop(() ->
        {
            final var array = array();
            randomInts(ALLOW_REPEATS, Count._32, array::add);
            map.put(array, 99);
            ensureEqual(99, map.get(array));
        });
    }

    @Test
    public void testFirstLast()
    {
        final var array = array();

        ensureThrows(array::first);
        ensureThrows(array::last);

        final var last = new MutableInteger(Integer.MIN_VALUE);

        resetIndex();
        randomInts(ALLOW_REPEATS, value ->
        {
            final int index = nextIndex();
            array.set(index, value);
            last.maximum(index);
            ensureEqual(array.get(0), array.first());
            ensureEqual(array.get(last.get()), array.last());
        });
    }

    @Test
    public void testGetSet()
    {
        final var array = array();

        resetIndex();
        randomInts(ALLOW_REPEATS, value ->
        {
            final int index = nextIndex();
            array.set(index, value);
            ensureEqual(array.get(index), value);
        });

        resetIndex();
        randomInts(ALLOW_REPEATS, value ->
        {
            final int index = nextIndex();
            array.set(index, value);
            ensureEqual(array.get(index), value);
        });

        array.clear();
        array.nullInt(-1);
        randomInts(NO_REPEATS, value -> value != -1, array::add);
        loop(() ->
        {
            final int index = randomInt(0, 100_000);
            final var value = array.safeGet(index);
            ensureEqual(index >= array.size(), array.isNull(value));
        });
    }

    @Test
    public void testIsNull()
    {
        final var array = array();
        final var nullValue = randomInt();
        array.nullInt(nullValue);
        ensure(array.hasNullInt());
        resetIndex();
        randomInts(ALLOW_REPEATS, value -> value != array.nullInt(), value ->
        {
            final int index = nextIndex();

            array.set(index, value);
            ensure(!array.isNull(array.get(index)));

            array.set(index, array.nullInt());
            ensure(array.isNull(array.get(index)));
        });
    }

    @Test
    public void testIteration()
    {
        final var array = array();
        array.hasNullInt(false);

        array.add(0);
        array.add(1);
        array.add(2);
        array.set(32, 100);

        var values = array.iterator();
        ensureEqual(0, values.next());
        ensureEqual(1, values.next());
        ensureEqual(2, values.next());
        ensureEqual(array.nullInt(), values.next());
        ensure(values.hasNext());

        array.hasNullInt(true);

        values = array.iterator();
        ensureEqual(1, values.next());
        ensureEqual(2, values.next());
        ensureEqual(100, values.next());
        ensureFalse(values.hasNext());
    }

    @Test
    public void testSerialization()
    {
        final var array = array();
        randomInts(ALLOW_REPEATS, array::add);
        serializationTest(array);
    }

    @Test
    public void testSizeIsEmpty()
    {
        final var array = array();

        ensure(array.isEmpty());
        ensure(array.size() == 0);
        array.add(0);
        ensure(array.size() == 1);
        array.add(1);
        ensure(array.size() == 2);
        array.add(2);
        ensure(array.size() == 3);
        array.set(1000, 1000);
        ensure(array.size() == 1001);
        array.clear(2);
        ensure(array.size() == 1001);
        array.clear();
        ensure(array.isEmpty());
        ensure(array.size() == 0);

        final var maximum = new MutableInteger(Integer.MIN_VALUE);
        resetIndex();
        randomInts(ALLOW_REPEATS, value ->
        {
            final int index = nextIndex();
            maximum.maximum(index);
            array.set(index, value);
            ensure(array.size() == maximum.get() + 1);
        });
    }

    @Test
    public void testSubArray()
    {
        final var array = array();
        randomInts(ALLOW_REPEATS, array::add);
        final var last = array.size() - 1;
        final int offset = Math.abs(randomInt(0, last));
        final int length = Math.abs(randomInt(0, last - offset));
        ensure(offset < array.size());
        ensure(length >= 0);
        ensure(offset + length < array.size());
        final var subArray = array.subArray(offset, length);
        for (int i = 0; i < length; i++)
        {
            ensureEqual(array.get(offset + i), subArray.get(i));
        }
    }

    private IntArray array()
    {
        final var array = new IntArray("test");
        array.initialize();
        return array;
    }
}
