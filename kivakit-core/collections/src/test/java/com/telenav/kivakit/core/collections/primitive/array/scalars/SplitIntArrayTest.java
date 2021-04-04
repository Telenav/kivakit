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

public class SplitIntArrayTest extends CoreCollectionsUnitTest
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
        iterations(1_000);
    }

    @Test
    public void testClear()
    {
        final var array = array();
        array.hasNullInt(false);
        randomInts(NO_REPEATS, Count.count(250), array::add);
        randomIndexes(NO_REPEATS, Count.count(250), index ->
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
        final var map = new HashMap<SplitIntArray, Integer>();
        loop(() ->
        {
            final var array = array();
            randomInts(ALLOW_REPEATS, Count._16, array::add);
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
            final var index = nextIndex();
            array.set(index, value);
            last.maximum(index);
            ensureEqual(array.get(0), array.first());
            ensureEqual(array.get(last.get()), array.last());
        });
    }

    @Test
    public void testGetSet()
    {
        {
            final var array = array();

            resetIndex();
            randomInts(ALLOW_REPEATS, value ->
            {
                final var index = nextIndex();
                array.set(index, value);
                ensureEqual(array.get(index), value);
            });

            resetIndex();
            randomInts(ALLOW_REPEATS, value ->
            {
                final var index = nextIndex();
                array.set(index, value);
                ensureEqual(array.get(index), value);
            });
        }
        {
            final var array = new SplitIntArray("test");
            array.nullInt(-1);
            array.initialize();

            randomInts(NO_REPEATS, value -> value != -1, array::add);
            loop(() ->
            {
                final var index = randomIndex();
                final var value = array.safeGet(index);
                ensureEqual(index >= array.size(), array.isNull(value));
            });
        }
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
            final var index = nextIndex();

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
        array.set(100, 100);

        var values = array.iterator();
        ensureEqual(0, values.next());
        ensureEqual(1, values.next());
        ensureEqual(2, values.next());
        ensureEqual(-1, values.next());
        ensure(values.hasNext());

        array.hasNullInt(true);
        array.nullInt(0);

        values = array.iterator();
        ensureEqual(1, values.next());
        ensureEqual(2, values.next());
        ensureEqual(-1, values.next());
        ensure(values.hasNext());
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
        }
        {
            final var array = array();
            final var maximum = new MutableInteger(Integer.MIN_VALUE);
            resetIndex();
            randomInts(ALLOW_REPEATS, value ->
            {
                final var index = nextIndex();
                maximum.maximum(index);
                array.set(index, value);
                ensure(array.size() == maximum.get() + 1);
            });
        }
    }

    private SplitIntArray array()
    {
        return (SplitIntArray) new SplitIntArray("test")
                .nullInt(-1)
                .initialChildSize(100)
                .initialize();
    }
}
