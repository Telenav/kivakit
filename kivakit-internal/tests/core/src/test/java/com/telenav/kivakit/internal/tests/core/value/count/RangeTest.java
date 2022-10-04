package com.telenav.kivakit.internal.tests.core.value.count;

import com.telenav.kivakit.core.value.count.MutableCount;
import com.telenav.kivakit.core.value.count.Range;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.value.count.Range.rangeExclusive;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;

/**
 * Unit test for {@link Range}
 *
 * @author jonathanl (shibo)
 */
public class RangeTest extends CoreUnitTest
{
    @Test
    public void testConstrain()
    {
        var inclusiveRange = rangeInclusive(count(100), count(200));
        for (int i = 0; i < 1000; i++)
        {
            ensure(inclusiveRange.constrained(count(i)).isBetweenInclusive(count(100), count(200)));
        }

        var exclusiveRange = rangeExclusive(count(100), count(200));
        for (int i = 0; i < 1000; i++)
        {
            ensure(exclusiveRange.constrained(count(i)).isBetweenInclusive(count(100), count(199)));
        }
    }

    @Test
    public void testContains()
    {
        for (int i = 1; i <= 10; i++)
        {
            ensure(rangeInclusive(count(1), count(10)).containsInclusive(count(i)));
        }
        ensure(!rangeInclusive(count(1), count(10)).containsInclusive(count(0)));
        ensure(!rangeInclusive(count(1), count(10)).containsInclusive(count(11)));

        for (int i = 1; i < 10; i++)
        {
            ensure(rangeExclusive(count(1), count(10)).containsInclusive(count(i)));
        }
        ensure(!rangeExclusive(count(1), count(10)).containsInclusive(count(0)));
        ensure(!rangeExclusive(count(1), count(10)).containsInclusive(count(10)));
    }

    @Test
    public void testExclusive()
    {
        var range = rangeExclusive(count(0), count(10));
        ensureEqual(range.minimum(), count(0));
        ensureEqual(range.inclusiveMaximum(), count(9));
        ensureEqual(range.exclusiveMaximum(), count(10));
    }

    @Test
    public void testForEach()
    {
        rangeInclusive(count(0), count(10)).forEach(value ->
                value.isBetweenInclusive(count(0), count(10)));

        rangeExclusive(count(0), count(10)).forEach(value ->
                value.isBetweenInclusive(count(0), count(9)));
    }

    @Test
    public void testInclusive()
    {
        var range = rangeInclusive(count(0), count(10));
        ensureEqual(range.minimum(), count(0));
        ensureEqual(range.inclusiveMaximum(), count(10));
        ensureEqual(range.exclusiveMaximum(), count(11));
    }

    @Test
    public void testLoop()
    {
        var counter = new MutableCount();
        rangeExclusive(count(0), count(10)).loop(counter::increment);
        ensureEqual(counter.asCount(), count(10));

        counter.set(0);
        rangeInclusive(count(0), count(10)).loop(counter::increment);
        ensureEqual(counter.asCount(), count(11));
    }

    @Test
    public void testRandomValue()
    {
        var exclusiveRange = rangeExclusive(count(0), count(100));

        random().loop(() ->
        {
            var value = exclusiveRange.randomValue(random().random());
            ensure(exclusiveRange.randomValue(random().random()).isBetweenExclusive(count(0), count(100)));
            ensureBetweenExclusive(value.asLong(), 0, 100);
        });

        var inclusiveRange = rangeInclusive(count(0), count(100));

        random().loop(() ->
        {
            var value = inclusiveRange.randomValue(random().random());
            ensure(value.isBetweenInclusive(count(0), count(100)));
            ensureBetweenInclusive(value.asLong(), 0, 100);
        });
    }

    @Test
    public void testSize()
    {
        var inclusive = rangeInclusive(count(0), count(10));
        ensureEqual(inclusive.size(), 11);

        var exclusive = rangeExclusive(count(0), count(10));
        ensureEqual(exclusive.size(), 10);
    }
}
