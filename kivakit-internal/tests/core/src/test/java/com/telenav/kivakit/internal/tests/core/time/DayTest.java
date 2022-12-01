package com.telenav.kivakit.internal.tests.core.time;

import com.telenav.kivakit.core.time.Day;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.core.time.Day.Type.DAY;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_UNIX_EPOCH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_YEAR;
import static com.telenav.kivakit.core.time.Day.day;
import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.core.time.Day.dayOfUnixEpoch;
import static com.telenav.kivakit.core.time.Day.dayOfYear;
import static com.telenav.kivakit.core.time.Day.isoDayOfWeek;
import static com.telenav.kivakit.core.time.Day.javaDayOfWeek;
import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.THURSDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.WEDNESDAY;
import static com.telenav.kivakit.core.value.count.Count._0;
import static com.telenav.kivakit.core.value.count.Count._1;
import static com.telenav.kivakit.core.value.count.Count._6;
import static com.telenav.kivakit.core.value.count.Count._7;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;

public class DayTest extends CoreUnitTest
{
    @Test
    public void testAsDayOfWeek()
    {
        ensureEqual(javaDayOfWeek(1).asDayOfWeek(), MONDAY);
        ensureEqual(javaDayOfWeek(2).asDayOfWeek(), TUESDAY);
        ensureEqual(javaDayOfWeek(3).asDayOfWeek(), WEDNESDAY);
        ensureEqual(javaDayOfWeek(4).asDayOfWeek(), THURSDAY);
        ensureEqual(javaDayOfWeek(5).asDayOfWeek(), FRIDAY);
        ensureEqual(javaDayOfWeek(6).asDayOfWeek(), SATURDAY);
        ensureEqual(javaDayOfWeek(7).asDayOfWeek(), SUNDAY);

        ensureEqual(isoDayOfWeek(0).asDayOfWeek(), MONDAY);
        ensureEqual(isoDayOfWeek(1).asDayOfWeek(), TUESDAY);
        ensureEqual(isoDayOfWeek(2).asDayOfWeek(), WEDNESDAY);
        ensureEqual(isoDayOfWeek(3).asDayOfWeek(), THURSDAY);
        ensureEqual(isoDayOfWeek(4).asDayOfWeek(), FRIDAY);
        ensureEqual(isoDayOfWeek(5).asDayOfWeek(), SATURDAY);
        ensureEqual(isoDayOfWeek(6).asDayOfWeek(), SUNDAY);
    }

    @Test
    public void testAsIndex()
    {
        ensureEqual(MONDAY.asDay().asIndex(), 0);
        ensureEqual(TUESDAY.asDay().asIndex(), 1);
        ensureEqual(WEDNESDAY.asDay().asIndex(), 2);
        ensureEqual(THURSDAY.asDay().asIndex(), 3);
        ensureEqual(FRIDAY.asDay().asIndex(), 4);
        ensureEqual(SATURDAY.asDay().asIndex(), 5);
        ensureEqual(SUNDAY.asDay().asIndex(), 6);

        ensureEqual(dayOfMonth(1).asIndex(), 0);
        ensureEqual(dayOfMonth(30).asIndex(), 29);
        ensureEqual(dayOfMonth(31).asIndex(), 30);

        ensureEqual(isoDayOfWeek(0).asIndex(), 0);
        ensureEqual(javaDayOfWeek(1).asIndex(), 0);
        ensureEqual(isoDayOfWeek(1).asIndex(), 1);
        ensureEqual(javaDayOfWeek(2).asIndex(), 1);
        ensureEqual(isoDayOfWeek(6).asIndex(), 6);
        ensureEqual(javaDayOfWeek(7).asIndex(), 6);
    }

    @Test
    public void testCreation()
    {
        ensureThrows(() -> day(-1));
        ensureEqual(day(5).type(), DAY);

        ensureThrows(() -> dayOfUnixEpoch(-1));
        ensureEqual(dayOfUnixEpoch(0).type(), DAY_OF_UNIX_EPOCH);

        ensureThrows(() -> dayOfYear(-1));
        ensureThrows(() -> dayOfYear(366));
        ensureEqual(dayOfYear(0).type(), DAY_OF_YEAR);
        ensureEqual(dayOfYear(365).type(), DAY_OF_YEAR);

        ensureEqual(javaDayOfWeek(1).asDayOfWeek(), MONDAY);
        ensureEqual(javaDayOfWeek(2).asDayOfWeek(), TUESDAY);
        ensureEqual(javaDayOfWeek(3).asDayOfWeek(), WEDNESDAY);
        ensureEqual(javaDayOfWeek(4).asDayOfWeek(), THURSDAY);
        ensureEqual(javaDayOfWeek(5).asDayOfWeek(), FRIDAY);
        ensureEqual(javaDayOfWeek(6).asDayOfWeek(), SATURDAY);
        ensureEqual(javaDayOfWeek(7).asDayOfWeek(), SUNDAY);

        ensureEqual(isoDayOfWeek(0).asDayOfWeek(), MONDAY);
        ensureEqual(isoDayOfWeek(1).asDayOfWeek(), TUESDAY);
        ensureEqual(isoDayOfWeek(2).asDayOfWeek(), WEDNESDAY);
        ensureEqual(isoDayOfWeek(3).asDayOfWeek(), THURSDAY);
        ensureEqual(isoDayOfWeek(4).asDayOfWeek(), FRIDAY);
        ensureEqual(isoDayOfWeek(5).asDayOfWeek(), SATURDAY);
        ensureEqual(isoDayOfWeek(6).asDayOfWeek(), SUNDAY);

        ensureThrows(() -> dayOfMonth(-1));
        ensureThrows(() -> dayOfMonth(0));
        ensureThrows(() -> dayOfMonth(32));

        ensureEqual(dayOfMonth(1).asUnits(), 1);
        ensureEqual(dayOfMonth(3).asUnits(), 3);
        ensureEqual(dayOfMonth(28).asUnits(), 28);
        ensureEqual(dayOfMonth(29).asUnits(), 29);
        ensureEqual(dayOfMonth(30).asUnits(), 30);
        ensureEqual(dayOfMonth(31).asUnits(), 31);
    }

    @Test
    public void testEquals()
    {
        ensureEqual(day(49), (day(49)));
        ensureEqual(isoDayOfWeek(4), (isoDayOfWeek(4)));
        ensureNotEqual(javaDayOfWeek(4), day(4));
        ensureNotEqual(javaDayOfWeek(4), day(4));

        var map = new HashMap<Day, Integer>();

        rangeInclusive(_1, _7).forEachInt(at -> map.put(javaDayOfWeek(at), at));
        rangeInclusive(_1, _7).forEachInt(at -> ensureEqual(map.get(javaDayOfWeek(at)), at));

        rangeInclusive(_0, _6).forEachInt(at -> map.put(isoDayOfWeek(at), at));
        rangeInclusive(_0, _6).forEachInt(at -> ensureEqual(map.get(isoDayOfWeek(at)), at));
    }
}
