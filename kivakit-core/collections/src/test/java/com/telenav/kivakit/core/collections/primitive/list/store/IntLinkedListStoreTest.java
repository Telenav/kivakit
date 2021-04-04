////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.list.store;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.kernel.language.values.mutable.MutableValue;
import org.junit.Test;

import java.util.Collections;

import static com.telenav.kivakit.core.test.UnitTest.Repeats.ALLOW_REPEATS;

public class IntLinkedListStoreTest extends CoreCollectionsUnitTest
{
    @Test
    public void test()
    {
        final var store = new IntLinkedListStore("test");
        store.initialize();

        var list = IntLinkedListStore.NEW_LIST;
        list = store.add(list, 1);
        list = store.add(list, 2);
        list = store.add(list, 3);
        {
            final var iterator = store.list(list);
            var value = 3;
            while (iterator.hasNext())
            {
                final var next = iterator.next();
                ensureEqual(value--, next);
            }
        }
        list = store.remove(list, 2);
        {
            final var iterator = store.list(list);
            ensure(iterator.hasNext());
            ensureEqual(3, iterator.next());
            ensure(iterator.hasNext());
            ensureEqual(1, iterator.next());
            ensureFalse(iterator.hasNext());
        }
        list = store.remove(list, 3);
        {
            final var iterator = store.list(list);
            ensure(iterator.hasNext());
            ensureEqual(1, iterator.next());
            ensureFalse(iterator.hasNext());
        }
        list = store.remove(list, 1);
        {
            final var iterator = store.list(list);
            ensureFalse(iterator.hasNext());
        }
    }

    @Test
    public void test2()
    {
        final var store = new IntLinkedListStore("test");
        store.initialize();

        var list = IntLinkedListStore.NEW_LIST;
        list = store.add(list, 1);
        list = store.add(list, 2);
        list = store.add(list, 3);
        {
            final var iterator = store.list(list);
            var value = 3;
            while (iterator.hasNext())
            {
                final var next = iterator.next();
                ensureEqual(value--, next);
            }
        }
        list = store.remove(list, 1);
        {
            final var iterator = store.list(list);
            ensure(iterator.hasNext());
            ensureEqual(3, iterator.next());
            ensure(iterator.hasNext());
            ensureEqual(2, iterator.next());
            ensureFalse(iterator.hasNext());
        }
        list = store.remove(list, 3);
        {
            final var iterator = store.list(list);
            ensure(iterator.hasNext());
            ensureEqual(2, iterator.next());
            ensureFalse(iterator.hasNext());
        }
        list = store.remove(list, 2);
        {
            final var iterator = store.list(list);
            ensureFalse(iterator.hasNext());
        }
    }

    @Test
    public void testRandom()
    {
        final var store = new IntLinkedListStore("test");
        store.initialize();

        final var list1 = new MutableValue<>(IntLinkedListStore.NEW_LIST);
        final var list2 = new MutableValue<>(IntLinkedListStore.NEW_LIST);

        final var values = randomIntList(ALLOW_REPEATS);
        values.forEach(value ->
        {
            list1.set(store.add(list1.get(), value));
            list2.set(store.add(list2.get(), value));
        });

        // The list will be backwards because of head insertion
        Collections.reverse(values);

        // Ensure list 1
        final var list1Values = store.list(list1.get());
        final var iterator1 = values.iterator();
        while (list1Values.hasNext())
        {
            final var value = list1Values.next();
            ensureEqual(iterator1.next(), value);
        }
        ensure(!iterator1.hasNext());

        // Ensure list 2
        final var list2Values = store.list(list1.get());
        final var iterator2 = values.iterator();
        while (list2Values.hasNext())
        {
            final var value = list2Values.next();
            ensureEqual(iterator2.next(), value);
        }
        ensure(!iterator2.hasNext());
    }
}
