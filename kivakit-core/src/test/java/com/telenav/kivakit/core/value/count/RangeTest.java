package com.telenav.kivakit.core.value.count;
import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.value.count.Range.exclusive;
import static com.telenav.kivakit.core.value.count.Range.inclusive;
import static com.telenav.kivakit.interfaces.code.FilteredLoopBody.FilterAction.ACCEPT;
import static com.telenav.kivakit.interfaces.code.FilteredLoopBody.FilterAction.REJECT;

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
        var inclusiveRange = inclusive(count(100), count(200));
        for (int i = 0; i < 1000; i++)
        {
            ensure(inclusiveRange.constrain(count(i)).isBetweenInclusive(count(100), count(200)));
        }

        var exclusiveRange = exclusive(count(100), count(200));
        for (int i = 0; i < 1000; i++)
        {
            ensure(exclusiveRange.constrain(count(i)).isBetweenInclusive(count(100), count(199)));
        }
    }

    @Test
    public void testContains()
    {
        for (int i = 1; i <= 10; i++)
        {
            ensure(inclusive(count(1), count(10)).contains(count(i)));
        }
        ensure(!inclusive(count(1), count(10)).contains(count(0)));
        ensure(!inclusive(count(1), count(10)).contains(count(11)));

        for (int i = 1; i < 10; i++)
        {
            ensure(exclusive(count(1), count(10)).contains(count(i)));
        }
        ensure(!exclusive(count(1), count(10)).contains(count(0)));
        ensure(!exclusive(count(1), count(10)).contains(count(10)));
    }

    @Test
    public void testExclusive()
    {
        var range = exclusive(count(0), count(10));
        ensureEqual(range.minimum(), count(0));
        ensureEqual(range.inclusiveMaximum(), count(9));
        ensureEqual(range.exclusiveMaximum(), count(10));
    }

    @Test
    public void testForCount()
    {
        var exclusiveRange = exclusive(count(0), count(10));
        var counter = new MutableCount();
        exclusiveRange.forCount(count(6), value ->
        {
            var acceptable = value.asInt() % 2 == 0;
            if (acceptable)
            {
                counter.increment();
            }
            return acceptable ? ACCEPT : REJECT;
        });
        ensureEqual(counter.asCount(), count(5));

        var inclusiveRange = inclusive(count(0), count(10));
        counter.set(0);
        inclusiveRange.forCount(count(6), value ->
        {
            var acceptable = value.asInt() % 2 == 0;
            if (acceptable)
            {
                counter.increment();
            }
            return acceptable ? ACCEPT : REJECT;
        });
        ensureEqual(counter.asCount(), count(6));
    }

    @Test
    public void testForEach()
    {
        inclusive(count(0), count(10)).forEach(value ->
                value.isBetweenInclusive(count(0), count(10)));

        exclusive(count(0), count(10)).forEach(value ->
                value.isBetweenInclusive(count(0), count(9)));
    }

    @Test
    public void testInclusive()
    {
        var range = inclusive(count(0), count(10));
        ensureEqual(range.minimum(), count(0));
        ensureEqual(range.inclusiveMaximum(), count(10));
        ensureEqual(range.exclusiveMaximum(), count(11));
    }

    @Test
    public void testSize()
    {
        var inclusive = inclusive(count(0), count(10));
        ensureEqual(inclusive.size(), 11);

        var exclusive = exclusive(count(0), count(10));
        ensureEqual(exclusive.size(), 10);
    }

    @Test
    public void testLoop()
    {
        var counter = new MutableCount();
        exclusive(count(0), count(10)).loop(counter::increment);
        ensureEqual(counter.asCount(), count(10));

        counter.set(0);
        inclusive(count(0), count(10)).loop(counter::increment);
        ensureEqual(counter.asCount(), count(11));
    }

    @Test
    public void testRandomValue()
    {
        var exclusiveRange = exclusive(count(0), count(100));

        random().loop(() ->
        {
            var value = exclusiveRange.randomValue();
            ensure(exclusiveRange.randomValue().isBetweenExclusive(count(0), count(100)));
            ensureBetweenExclusive(value.asLong(), 0, 100);
        });

        var inclusiveRange = inclusive(count(0), count(100));

        random().loop(() ->
        {
            var value = inclusiveRange.randomValue();
            ensure(value.isBetweenInclusive(count(0), count(100)));
            ensureBetweenInclusive(value.asLong(), 0, 100);
        });
    }
}
