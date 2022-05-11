package com.telenav.kivakit.core.tests.time;

import com.telenav.kivakit.core.test.support.CoreUnitTest;
import com.telenav.kivakit.core.time.Hour;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Meridiem.AM;
import static com.telenav.kivakit.core.time.Meridiem.PM;
import static com.telenav.kivakit.core.time.Meridiem.meridiem;
import static com.telenav.kivakit.core.time.Meridiem.meridiemHour;

/**
 * Unit test for {@link Hour}
 *
 * @author jonathanl (shibo)
 */
public class MeridiemTest extends CoreUnitTest
{
    @Test
    public void testAsMilitaryHour()
    {
        ensureThrows(() -> AM.asMilitaryHour(0));
        ensureThrows(() -> AM.asMilitaryHour(13));

        ensureEqual(AM.asMilitaryHour(12), 0);
        ensureEqual(AM.asMilitaryHour(1), 1);
        ensureEqual(AM.asMilitaryHour(2), 2);
        ensureEqual(AM.asMilitaryHour(3), 3);
        ensureEqual(AM.asMilitaryHour(4), 4);
        ensureEqual(AM.asMilitaryHour(5), 5);
        ensureEqual(AM.asMilitaryHour(6), 6);
        ensureEqual(AM.asMilitaryHour(7), 7);
        ensureEqual(AM.asMilitaryHour(8), 8);
        ensureEqual(AM.asMilitaryHour(9), 9);
        ensureEqual(AM.asMilitaryHour(10), 10);
        ensureEqual(AM.asMilitaryHour(11), 11);

        ensureEqual(PM.asMilitaryHour(12), 12);
        ensureEqual(PM.asMilitaryHour(1), 13);
        ensureEqual(PM.asMilitaryHour(2), 14);
        ensureEqual(PM.asMilitaryHour(3), 15);
        ensureEqual(PM.asMilitaryHour(4), 16);
        ensureEqual(PM.asMilitaryHour(5), 17);
        ensureEqual(PM.asMilitaryHour(6), 18);
        ensureEqual(PM.asMilitaryHour(7), 19);
        ensureEqual(PM.asMilitaryHour(8), 20);
        ensureEqual(PM.asMilitaryHour(9), 21);
        ensureEqual(PM.asMilitaryHour(10), 22);
        ensureEqual(PM.asMilitaryHour(11), 23);
    }

    @Test
    public void testCreation()
    {
        ensureThrows(() -> meridiem(-1));
        ensureThrows(() -> meridiem(24));

        for (var military = 0; military < 12; military++)
        {
            ensure(meridiem(military) == AM);
        }
        for (var military = 12; military < 24; military++)
        {
            ensure(meridiem(military) == PM);
        }
    }

    @Test
    public void testMeridiemHour()
    {
        ensureThrows(() -> meridiemHour(-1));
        ensureThrows(() -> meridiemHour(24));

        ensureEqual(meridiemHour(0), 12);
        ensureEqual(meridiemHour(1), 1);
        ensureEqual(meridiemHour(2), 2);
        ensureEqual(meridiemHour(3), 3);
        ensureEqual(meridiemHour(4), 4);
        ensureEqual(meridiemHour(5), 5);
        ensureEqual(meridiemHour(6), 6);
        ensureEqual(meridiemHour(7), 7);
        ensureEqual(meridiemHour(8), 8);
        ensureEqual(meridiemHour(9), 9);
        ensureEqual(meridiemHour(10), 10);
        ensureEqual(meridiemHour(11), 11);
        ensureEqual(meridiemHour(12), 12);
        ensureEqual(meridiemHour(13), 1);
        ensureEqual(meridiemHour(14), 2);
        ensureEqual(meridiemHour(15), 3);
        ensureEqual(meridiemHour(16), 4);
        ensureEqual(meridiemHour(17), 5);
        ensureEqual(meridiemHour(18), 6);
        ensureEqual(meridiemHour(19), 7);
        ensureEqual(meridiemHour(20), 8);
        ensureEqual(meridiemHour(21), 9);
        ensureEqual(meridiemHour(22), 10);
        ensureEqual(meridiemHour(23), 11);
    }
}
