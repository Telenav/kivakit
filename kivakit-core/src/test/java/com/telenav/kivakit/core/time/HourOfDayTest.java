package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.HourOfDay.am;
import static com.telenav.kivakit.core.time.HourOfDay.hourOfDay;
import static com.telenav.kivakit.core.time.HourOfDay.midnight;
import static com.telenav.kivakit.core.time.HourOfDay.noon;
import static com.telenav.kivakit.core.time.HourOfDay.pm;

/**
 * Unit test for {@link HourOfDay}
 *
 * @author jonathanl (shibo)
 */
public class HourOfDayTest extends UnitTest
{
    @Test
    public void testAm()
    {
        ensureEqual(am(8), hourOfDay(8));
    }

    @Test
    public void testHourOfDay()
    {
        ensureEqual(hourOfDay(20).asInt(), 20);
    }

    @Test
    public void testMaximum()
    {
        ensureEqual(am(0).maximum(), hourOfDay(23));
    }

    @Test
    public void testMidnight()
    {
        ensureEqual(midnight(), hourOfDay(0));
    }

    @Test
    public void testMinimum()
    {
        ensureEqual(am(0).minimum(), am(0));
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
        ensureEqual(noon(), hourOfDay(12));
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
        ensureEqual(pm(8), hourOfDay(20));
    }
}
