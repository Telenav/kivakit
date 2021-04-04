////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.set;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

public class LongSetTest extends CoreCollectionsUnitTest
{
    @FunctionalInterface
    interface MapTest
    {
        void test(LongSet set, List<Long> values);
    }

    @Test
    public void testClear()
    {
        withPopulatedSet((set, values) ->
        {
            ensureFalse(set.isEmpty());
            set.clear();
            ensure(set.isEmpty());
        });
    }

    @Test
    public void testContains()
    {
        withPopulatedSet((set, values) -> values.forEach(value -> ensure(set.contains(value))));
    }

    @Test
    public void testEqualsHashCode()
    {
        withPopulatedSet((a, values) ->
        {
            final var b = set();
            addAll(b, values);
            ensureEqual(a, b);
            b.add(-1);
            ensureNotEqual(a, b);
        });
    }

    @Test
    public void testFreeze()
    {
        withPopulatedSet((a, values) ->
        {
            final var b = set();
            addAll(b, values);
            ensureEqual(a, b);
            b.compress(CompressibleCollection.Method.FREEZE);
            ensureEqual(a, b);
        });
        withPopulatedSet((a, values) ->
        {
            final var b = set();
            addAll(b, values);
            addAll(b, values);
            ensureEqual(a, b);
            b.compress(CompressibleCollection.Method.FREEZE);
            ensureEqual(a, b);
        });
    }

    @Test
    public void testRemove()
    {
        withPopulatedSet((set, values) ->
                values.forEach(value ->
                {
                    final var size = set.size();
                    final var exists = set.contains(value);
                    set.remove(value);
                    ensure(!set.contains(value));
                    if (exists)
                    {
                        ensureEqual(set.size(), size - 1);
                    }
                }));
    }

    @Test
    public void testSerialization()
    {
        withPopulatedSet((set, values) -> serializationTest(set));
    }

    @Test
    public void testValues()
    {
        withPopulatedSet((set, values) ->
        {
            final var iterator = set.values();
            var count = 0;
            final var valueSet = new HashSet<>(values);
            while (iterator.hasNext())
            {
                final var value = iterator.next();
                ensure(valueSet.contains(value));
                count++;
            }
            ensureEqual(count, set.size());
        });
    }

    private void addAll(final LongSet set, final List<Long> values)
    {
        values.forEach(set::add);
    }

    private LongSet set()
    {
        final var set = new LongSet("test");
        set.initialize();
        return set;
    }

    private void withPopulatedSet(final MapTest test)
    {
        final var set = set();
        final var values = randomLongList(Repeats.NO_REPEATS);
        addAll(set, values);
        test.test(set, values);
    }
}
