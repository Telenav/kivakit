package com.telenav.kivakit.internal.tests.core.time;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.time.Hour;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Meridiem.AM;
import static com.telenav.kivakit.core.time.Meridiem.PM;
import static com.telenav.kivakit.core.time.Meridiem.meridiem;
import static com.telenav.kivakit.core.time.Meridiem.asMeridiemHour;

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
        ensureThrows(() -> asMeridiemHour(-1));
        ensureThrows(() -> asMeridiemHour(24));

        ensureEqual(asMeridiemHour(0), 12);
        ensureEqual(asMeridiemHour(1), 1);
        ensureEqual(asMeridiemHour(2), 2);
        ensureEqual(asMeridiemHour(3), 3);
        ensureEqual(asMeridiemHour(4), 4);
        ensureEqual(asMeridiemHour(5), 5);
        ensureEqual(asMeridiemHour(6), 6);
        ensureEqual(asMeridiemHour(7), 7);
        ensureEqual(asMeridiemHour(8), 8);
        ensureEqual(asMeridiemHour(9), 9);
        ensureEqual(asMeridiemHour(10), 10);
        ensureEqual(asMeridiemHour(11), 11);
        ensureEqual(asMeridiemHour(12), 12);
        ensureEqual(asMeridiemHour(13), 1);
        ensureEqual(asMeridiemHour(14), 2);
        ensureEqual(asMeridiemHour(15), 3);
        ensureEqual(asMeridiemHour(16), 4);
        ensureEqual(asMeridiemHour(17), 5);
        ensureEqual(asMeridiemHour(18), 6);
        ensureEqual(asMeridiemHour(19), 7);
        ensureEqual(asMeridiemHour(20), 8);
        ensureEqual(asMeridiemHour(21), 9);
        ensureEqual(asMeridiemHour(22), 10);
        ensureEqual(asMeridiemHour(23), 11);
    }
}
