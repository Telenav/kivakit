package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.core.time.Hour.am;
import static com.telenav.kivakit.core.time.Hour.hour;
import static com.telenav.kivakit.core.time.Hour.midnight;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.Hour.noon;
import static com.telenav.kivakit.core.time.Hour.pm;
import static com.telenav.kivakit.core.value.count.Count._1;
import static com.telenav.kivakit.core.value.count.Count._12;
import static com.telenav.kivakit.core.value.count.Count._23;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;

/**
 * Unit test for {@link Hour}
 *
 * @author jonathanl (shibo)
 */
public class HourTest extends CoreUnitTest
{
    @Test
    public void testAm()
    {
        ensureThrows(() -> am(-1));
        ensureThrows(() -> am(0));
        ensureThrows(() -> am(13));

        ensureEqual(am(12), militaryHour(0));
        ensureEqual(am(1), militaryHour(1));
        ensureEqual(am(2), militaryHour(2));
        ensureEqual(am(3), militaryHour(3));
        ensureEqual(am(4), militaryHour(4));
        ensureEqual(am(5), militaryHour(5));
        ensureEqual(am(6), militaryHour(6));
        ensureEqual(am(7), militaryHour(7));
        ensureEqual(am(8), militaryHour(8));
        ensureEqual(am(9), militaryHour(9));
        ensureEqual(am(10), militaryHour(10));
        ensureEqual(am(11), militaryHour(11));
    }

    @Test
    public void testAsMeridiemHour()
    {
        ensureThrows(() -> hour(24).asMeridiemHour());
        ensureThrows(() -> hour(1024).asMeridiemHour());

        ensureEqual(am(12).asMeridiemHour(), 12);
        ensureEqual(am(1).asMeridiemHour(), 1);
        ensureEqual(am(2).asMeridiemHour(), 2);
        ensureEqual(am(3).asMeridiemHour(), 3);
        ensureEqual(am(4).asMeridiemHour(), 4);
        ensureEqual(am(5).asMeridiemHour(), 5);
        ensureEqual(am(6).asMeridiemHour(), 6);
        ensureEqual(am(7).asMeridiemHour(), 7);
        ensureEqual(am(8).asMeridiemHour(), 8);
        ensureEqual(am(9).asMeridiemHour(), 9);
        ensureEqual(am(10).asMeridiemHour(), 10);
        ensureEqual(am(11).asMeridiemHour(), 11);

        ensureEqual(pm(12).asMeridiemHour(), 12);
        ensureEqual(pm(1).asMeridiemHour(), 1);
        ensureEqual(pm(2).asMeridiemHour(), 2);
        ensureEqual(pm(3).asMeridiemHour(), 3);
        ensureEqual(pm(4).asMeridiemHour(), 4);
        ensureEqual(pm(5).asMeridiemHour(), 5);
        ensureEqual(pm(6).asMeridiemHour(), 6);
        ensureEqual(pm(7).asMeridiemHour(), 7);
        ensureEqual(pm(8).asMeridiemHour(), 8);
        ensureEqual(pm(9).asMeridiemHour(), 9);
        ensureEqual(pm(10).asMeridiemHour(), 10);
        ensureEqual(pm(11).asMeridiemHour(), 11);

        ensureEqual(militaryHour(0).asMeridiemHour(), 12);
        ensureEqual(militaryHour(1).asMeridiemHour(), 1);
        ensureEqual(militaryHour(2).asMeridiemHour(), 2);
        ensureEqual(militaryHour(3).asMeridiemHour(), 3);
        ensureEqual(militaryHour(4).asMeridiemHour(), 4);
        ensureEqual(militaryHour(5).asMeridiemHour(), 5);
        ensureEqual(militaryHour(6).asMeridiemHour(), 6);
        ensureEqual(militaryHour(7).asMeridiemHour(), 7);
        ensureEqual(militaryHour(8).asMeridiemHour(), 8);
        ensureEqual(militaryHour(9).asMeridiemHour(), 9);
        ensureEqual(militaryHour(10).asMeridiemHour(), 10);
        ensureEqual(militaryHour(11).asMeridiemHour(), 11);
        ensureEqual(militaryHour(12).asMeridiemHour(), 12);
        ensureEqual(militaryHour(13).asMeridiemHour(), 1);
        ensureEqual(militaryHour(14).asMeridiemHour(), 2);
        ensureEqual(militaryHour(15).asMeridiemHour(), 3);
        ensureEqual(militaryHour(16).asMeridiemHour(), 4);
        ensureEqual(militaryHour(17).asMeridiemHour(), 5);
        ensureEqual(militaryHour(18).asMeridiemHour(), 6);
        ensureEqual(militaryHour(19).asMeridiemHour(), 7);
        ensureEqual(militaryHour(20).asMeridiemHour(), 8);
        ensureEqual(militaryHour(21).asMeridiemHour(), 9);
        ensureEqual(militaryHour(22).asMeridiemHour(), 10);
        ensureEqual(militaryHour(23).asMeridiemHour(), 11);
    }

    @Test
    public void testAsMilitaryHour()
    {
        ensureEqual(am(12).asMilitaryHour(), 0);
        ensureEqual(am(1).asMilitaryHour(), 1);
        ensureEqual(am(2).asMilitaryHour(), 2);
        ensureEqual(am(3).asMilitaryHour(), 3);
        ensureEqual(am(4).asMilitaryHour(), 4);
        ensureEqual(am(5).asMilitaryHour(), 5);
        ensureEqual(am(6).asMilitaryHour(), 6);
        ensureEqual(am(7).asMilitaryHour(), 7);
        ensureEqual(am(8).asMilitaryHour(), 8);
        ensureEqual(am(9).asMilitaryHour(), 9);
        ensureEqual(am(10).asMilitaryHour(), 10);
        ensureEqual(am(11).asMilitaryHour(), 11);
        ensureEqual(pm(12).asMilitaryHour(), 12);
        ensureEqual(pm(1).asMilitaryHour(), 13);
        ensureEqual(pm(2).asMilitaryHour(), 14);
        ensureEqual(pm(3).asMilitaryHour(), 15);
        ensureEqual(pm(4).asMilitaryHour(), 16);
        ensureEqual(pm(5).asMilitaryHour(), 17);
        ensureEqual(pm(6).asMilitaryHour(), 18);
        ensureEqual(pm(7).asMilitaryHour(), 19);
        ensureEqual(pm(8).asMilitaryHour(), 20);
        ensureEqual(pm(9).asMilitaryHour(), 21);
        ensureEqual(pm(10).asMilitaryHour(), 22);
        ensureEqual(pm(11).asMilitaryHour(), 23);

        ensureEqual(militaryHour(0).asMilitaryHour(), 0);
        ensureEqual(militaryHour(1).asMilitaryHour(), 1);
        ensureEqual(militaryHour(2).asMilitaryHour(), 2);
        ensureEqual(militaryHour(3).asMilitaryHour(), 3);
        ensureEqual(militaryHour(4).asMilitaryHour(), 4);
        ensureEqual(militaryHour(5).asMilitaryHour(), 5);
        ensureEqual(militaryHour(6).asMilitaryHour(), 6);
        ensureEqual(militaryHour(7).asMilitaryHour(), 7);
        ensureEqual(militaryHour(8).asMilitaryHour(), 8);
        ensureEqual(militaryHour(9).asMilitaryHour(), 9);
        ensureEqual(militaryHour(10).asMilitaryHour(), 10);
        ensureEqual(militaryHour(11).asMilitaryHour(), 11);
        ensureEqual(militaryHour(12).asMilitaryHour(), 12);
        ensureEqual(militaryHour(13).asMilitaryHour(), 13);
        ensureEqual(militaryHour(14).asMilitaryHour(), 14);
        ensureEqual(militaryHour(15).asMilitaryHour(), 15);
        ensureEqual(militaryHour(16).asMilitaryHour(), 16);
        ensureEqual(militaryHour(17).asMilitaryHour(), 17);
        ensureEqual(militaryHour(18).asMilitaryHour(), 18);
        ensureEqual(militaryHour(19).asMilitaryHour(), 19);
        ensureEqual(militaryHour(20).asMilitaryHour(), 20);
        ensureEqual(militaryHour(21).asMilitaryHour(), 21);
        ensureEqual(militaryHour(22).asMilitaryHour(), 22);
        ensureEqual(militaryHour(23).asMilitaryHour(), 23);
    }

    @Test
    public void testEquals()
    {
        var map = new HashMap<Hour, Integer>();

        rangeInclusive(_1, _12).forEachInt(at -> map.put(am(at), at));
        rangeInclusive(_1, _12).forEachInt(at -> ensureEqual(map.get(am(at)), at));

        rangeInclusive(_1, _12).forEachInt(at -> map.put(pm(at), at));
        rangeInclusive(_1, _12).forEachInt(at -> ensureEqual(map.get(pm(at)), at));

        rangeInclusive(_12, _23).forEachInt(at -> map.put(militaryHour(at), at));
        rangeInclusive(_12, _23).forEachInt(at -> ensureEqual(map.get(militaryHour(at)), at));
    }

    @Test
    public void testHour()
    {
        ensureThrows(() -> hour(-1));

        ensureEqual(hour(8000).asInt(), 8000);
    }

    @Test
    public void testIsMeridiem()
    {
        ensure(am(12).isMeridiem());
        ensure(pm(11).isMeridiem());
        ensure(!hour(27).isMeridiem());
        ensure(!militaryHour(7).isMeridiem());
    }

    @Test
    public void testIsMilitary()
    {
        ensure(!am(12).isMilitary());
        ensure(!pm(11).isMilitary());
        ensure(!hour(27).isMilitary());
        ensure(militaryHour(7).isMilitary());
    }

    @Test
    public void testMaximum()
    {
        ensureEqual(am(12).maximum(), militaryHour(23));
    }

    @Test
    public void testMidnight()
    {
        ensureEqual(midnight(), militaryHour(0));
        ensureEqual(midnight(), am(12));
    }

    @Test
    public void testMilitaryHour()
    {
        ensureThrows(() -> militaryHour(-1));
        ensureThrows(() -> militaryHour(24));

        for (var hour = 0; hour < 24; hour++)
        {
            ensureEqual(militaryHour(hour).asInt(), hour);
            ensureEqual(militaryHour(hour).asMilitaryHour(), hour);
        }
    }

    @Test
    public void testMinimum()
    {
        ensureEqual(am(12).minimum(), am(12));
    }

    @Test
    public void testMinus()
    {
        ensureEqual(pm(8).minus(7), pm(1));
        ensureEqual(am(3).minus(6), pm(9));
    }

    @Test
    public void testNoon()
    {
        ensureEqual(noon(), militaryHour(12));
        ensureEqual(noon(), pm(12));
    }

    @Test
    public void testPlus()
    {
        ensureEqual(am(8).plus(5), pm(1));
        ensureEqual(pm(9).plus(6), am(3));
    }

    @Test
    public void testPm()
    {
        ensureEqual(pm(8), militaryHour(20));
    }

    @Test
    public void testToString()
    {
        ensureEqual(pm(8).toString(), "8pm");
        ensureEqual(pm(12).toString(), "12pm");
        ensureEqual(pm(1).toString(), "1pm");
        ensureEqual(am(8).toString(), "8am");
        ensureEqual(am(12).toString(), "12am");
        ensureEqual(militaryHour(0).toString(), "12am");
        ensureEqual(militaryHour(1).toString(), "1am");
        ensureEqual(militaryHour(12).toString(), "12pm");
        ensureEqual(militaryHour(13).toString(), "1pm");
    }
}
