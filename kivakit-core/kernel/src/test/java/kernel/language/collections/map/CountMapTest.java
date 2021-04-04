////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.collections.map;

import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class CountMapTest
{
    @Test
    public void test()
    {
        final var count1 = new CountMap<String>();
        count1.increment("foo");
        ensureEqual(1, count1.count("foo").asInt());
        count1.increment("foo");
        ensureEqual(2, count1.count("foo").asInt());
        count1.increment("bar");
        ensureEqual(1, count1.count("bar").asInt());
        ensureEqual(3L, count1.total());

        final var count2 = new CountMap<String>();
        count2.increment("foo");
        ensureEqual(1, count2.count("foo").asInt());
        count2.increment("foo");
        count1.increment("bar");
        ensureEqual(2L, count2.total());
        ensureEqual(2, count2.count("foo").asInt());
        ensureEqual(4L, count1.total());

        final var count3 = new CountMap<String>();
        count3.mergeIn(count1);
        count3.mergeIn(count2);
        count1.increment("bar");
        count1.increment("baz");
        ensureEqual(1, count1.count("baz").asInt());
        ensureEqual(4, count3.count("foo").asInt());
        ensureEqual(2, count2.count("foo").asInt());
        ensureEqual(2, count1.count("foo").asInt());
        ensureEqual(3, count1.count("bar").asInt());
        ensureEqual(1, count1.count("baz").asInt());
        ensureEqual(6L, count1.total());
        ensureEqual(2L, count2.total());
        ensureEqual(6L, count3.total());

        final var count4 = new CountMap<String>();
        count4.mergeIn(count3);
        count4.mergeIn(count3);
        ensureEqual(12L, count4.total());
        ensureEqual(8, count4.count("foo").asInt());
        ensureEqual(4, count3.count("foo").asInt());
        ensureEqual(2, count2.count("foo").asInt());
        ensureEqual(2, count1.count("foo").asInt());
        ensureEqual(3, count1.count("bar").asInt());
        ensureEqual(1, count1.count("baz").asInt());
        ensureEqual(6L, count1.total());
    }
}
