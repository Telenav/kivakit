package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Day.Type.DAY;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_UNIX_EPOCH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_YEAR;
import static com.telenav.kivakit.core.time.Day.day;
import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.core.time.Day.dayOfUnixEpoch;
import static com.telenav.kivakit.core.time.Day.dayOfWeek;
import static com.telenav.kivakit.core.time.Day.dayOfYear;
import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.ISO;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.JAVA;
import static com.telenav.kivakit.core.time.DayOfWeek.THURSDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.WEDNESDAY;

public class DayTest extends CoreUnitTest
{
    @Test
    public void testAsDayOfWeek()
    {
        ensureEqual(dayOfWeek(1, JAVA).asDayOfWeek(), MONDAY);
        ensureEqual(dayOfWeek(2, JAVA).asDayOfWeek(), TUESDAY);
        ensureEqual(dayOfWeek(3, JAVA).asDayOfWeek(), WEDNESDAY);
        ensureEqual(dayOfWeek(4, JAVA).asDayOfWeek(), THURSDAY);
        ensureEqual(dayOfWeek(5, JAVA).asDayOfWeek(), FRIDAY);
        ensureEqual(dayOfWeek(6, JAVA).asDayOfWeek(), SATURDAY);
        ensureEqual(dayOfWeek(7, JAVA).asDayOfWeek(), SUNDAY);

        ensureEqual(dayOfWeek(0, ISO).asDayOfWeek(), MONDAY);
        ensureEqual(dayOfWeek(1, ISO).asDayOfWeek(), TUESDAY);
        ensureEqual(dayOfWeek(2, ISO).asDayOfWeek(), WEDNESDAY);
        ensureEqual(dayOfWeek(3, ISO).asDayOfWeek(), THURSDAY);
        ensureEqual(dayOfWeek(4, ISO).asDayOfWeek(), FRIDAY);
        ensureEqual(dayOfWeek(5, ISO).asDayOfWeek(), SATURDAY);
        ensureEqual(dayOfWeek(6, ISO).asDayOfWeek(), SUNDAY);
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

        ensureEqual(dayOfWeek(0, ISO).asIndex(), 0);
        ensureEqual(dayOfWeek(1, JAVA).asIndex(), 0);
        ensureEqual(dayOfWeek(1, ISO).asIndex(), 1);
        ensureEqual(dayOfWeek(2, JAVA).asIndex(), 1);
        ensureEqual(dayOfWeek(6, ISO).asIndex(), 6);
        ensureEqual(dayOfWeek(7, JAVA).asIndex(), 6);
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

        ensureEqual(dayOfWeek(1, JAVA).asDayOfWeek(), MONDAY);
        ensureEqual(dayOfWeek(2, JAVA).asDayOfWeek(), TUESDAY);
        ensureEqual(dayOfWeek(3, JAVA).asDayOfWeek(), WEDNESDAY);
        ensureEqual(dayOfWeek(4, JAVA).asDayOfWeek(), THURSDAY);
        ensureEqual(dayOfWeek(5, JAVA).asDayOfWeek(), FRIDAY);
        ensureEqual(dayOfWeek(6, JAVA).asDayOfWeek(), SATURDAY);
        ensureEqual(dayOfWeek(7, JAVA).asDayOfWeek(), SUNDAY);

        ensureEqual(dayOfWeek(0, ISO).asDayOfWeek(), MONDAY);
        ensureEqual(dayOfWeek(1, ISO).asDayOfWeek(), TUESDAY);
        ensureEqual(dayOfWeek(2, ISO).asDayOfWeek(), WEDNESDAY);
        ensureEqual(dayOfWeek(3, ISO).asDayOfWeek(), THURSDAY);
        ensureEqual(dayOfWeek(4, ISO).asDayOfWeek(), FRIDAY);
        ensureEqual(dayOfWeek(5, ISO).asDayOfWeek(), SATURDAY);
        ensureEqual(dayOfWeek(6, ISO).asDayOfWeek(), SUNDAY);

        ensureThrows(() -> dayOfMonth(-1));
        ensureThrows(() -> dayOfMonth(0));
        ensureThrows(() -> dayOfMonth(32));

        ensureEqual(dayOfMonth(1).asInt(), 1);
        ensureEqual(dayOfMonth(3).asInt(), 3);
        ensureEqual(dayOfMonth(28).asInt(), 28);
        ensureEqual(dayOfMonth(29).asInt(), 29);
        ensureEqual(dayOfMonth(30).asInt(), 30);
        ensureEqual(dayOfMonth(31).asInt(), 31);
    }

    @Test
    public void testEquals()
    {
        ensureEqual(day(49), (day(49)));
        ensureEqual(dayOfWeek(4, JAVA), (dayOfWeek(4, JAVA)));
        ensureNotEqual(dayOfWeek(4, JAVA), (day(4)));
        ensureNotEqual(dayOfWeek(4, ISO), (day(4)));
    }
}
