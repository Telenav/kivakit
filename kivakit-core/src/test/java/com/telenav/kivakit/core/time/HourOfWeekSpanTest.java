package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.CoreUnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.time.ZoneId;
import java.util.HashMap;

import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.Hour.am;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.Hour.pm;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.time.HourOfWeekSpan.hourOfWeekSpan;
import static com.telenav.kivakit.core.time.HourOfWeekSpan.hourOfWeekSpanUtc;
import static com.telenav.kivakit.core.time.Minute.minute;
import static com.telenav.kivakit.core.time.Month.APRIL;
import static com.telenav.kivakit.core.time.Second.second;
import static com.telenav.kivakit.core.time.Year.year;
import static com.telenav.kivakit.core.value.count.Count._0;
import static com.telenav.kivakit.core.value.count.Range.rangeExclusive;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;

/**
 * Unit test for {@link HourOfWeekSpan}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class HourOfWeekSpanTest extends CoreUnitTest
{
    @Test
    public void testAsLocalTime()
    {
        // Monday 3am to Saturday at 11 in UTC should be
        // Sunday at 8pm to Saturday at 4pm in local time (-07:00)
        var start = hourOfWeek(SUNDAY, pm(8));
        var end = hourOfWeek(SATURDAY, pm(4));
        var local = hourOfWeekSpan(start, end, localZone());

        var utcSpan = utcSpan();

        ensureEqual(utcSpan.asLocalTime(localZone()), local);
    }

    @Test
    public void testAsUtc()
    {
        // Monday 3am to Saturday midnight in localtime (-07:00)
        // should be Monday at 10am to Sunday at 7am in UTC time
        var start = hourOfWeek(MONDAY, am(10));
        var end = hourOfWeek(SUNDAY, am(6));
        ensureEqual(localSpan().asUtc(), hourOfWeekSpanUtc(start, end));
        ensureEqual(localSpan().asUtc().asUtc(), hourOfWeekSpanUtc(start, end));
        ensureEqual(utcSpan().asUtc(), utcSpan());
    }

    @Test
    public void testCreation()
    {
        var span = hourOfWeekSpan(hourOfWeek(SUNDAY, am(10)), hourOfWeek(MONDAY, am(10)), localZone());
        ensureEqual(span.startHourOfWeek().dayOfWeek(), SUNDAY);
        ensureEqual(span.endHourOfWeek().dayOfWeek(), MONDAY);
    }

    @Test
    public void testEquals()
    {
        var map = new HashMap<HourOfWeek, Integer>();

        rangeExclusive(_0, count(24 * 7)).forEachInt(at -> map.put(hourOfWeek(at), at));
        rangeExclusive(_0, count(24 * 7)).forEachInt(at -> ensureEqual(map.get(hourOfWeek(at)), at));
    }

    @Test
    public void testIncludes()
    {
        // Right included: Monday at 3am to Saturday at 11pm in UTC should include Saturday at 11pm
        ensure(utcSpan().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(9),
                militaryHour(23),
                minute(0),
                second(0))));

        // Right excluded: Monday at 3am to Saturday at 11pm in UTC should not include Sunday at midnight
        ensure(!utcSpan().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(10),
                militaryHour(0),
                minute(0),
                second(0))));

        // Left included: Monday at 3am to Saturday at 11pm in UTC should include Monday at 3am
        ensure(utcSpan().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(4),
                militaryHour(3),
                minute(0),
                second(0))));

        // Left excluded: Monday at 3am to Saturday at 11pm in UTC should include Monday at 2am
        ensure(!utcSpan().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(4),
                militaryHour(2),
                minute(0),
                second(0))));

        // Center included: Monday at 3am to Saturday at 11pm in UTC should include all times betweeen
        rangeInclusive(MONDAY.at(am(3)), SATURDAY.at(pm(11)))
                .forEach(at -> ensure(utcSpan().includes(at.asEpochTime())));

        // Center included: Monday at 3am to Saturday at 11pm in UTC should not include times after Saturday at 11pm
        rangeInclusive(SUNDAY.at(am(1)), SUNDAY.at(pm(11)))
                .forEach(at -> ensure(!utcSpan().includes(at.asEpochTime())));

        //  sat at 3am to tues at 11pm, should include sun at midnight
        ensure(utcSpanWrapping().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(3),
                militaryHour(0),
                minute(0),
                second(0))));

        //  sat at 3am to tues at 11pm, should include sat at 3am
        ensure(utcSpanWrapping().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(9),
                militaryHour(3),
                minute(0),
                second(0))));

        // sat at 3am to tues at 11pm should include tues 5 at 11pm
        ensure(utcSpanWrapping().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(5),
                militaryHour(23),
                minute(0),
                second(0))));

        //  sat at 3am to tues at 11pm should not include fri at midnight
        ensure(!utcSpanWrapping().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(8),
                militaryHour(0),
                minute(0),
                second(0))));

        // sat at 3am to tues at 11pm should include sat 9 at 2am
        ensure(!utcSpanWrapping().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(9),
                militaryHour(2),
                minute(0),
                second(0))));

        // sat at 3am to tues at 11pm should include wed at midnight
        ensure(!utcSpanWrapping().includes(LocalTime.localTime(utc(),
                year(2022),
                APRIL,
                dayOfMonth(6),
                militaryHour(0),
                minute(0),
                second(0))));
    }

    @Test
    public void testIsLocal()
    {
        ensure(localSpan().isLocal());
        ensure(!localSpan().isUtc());

        ensure(utcSpan().isUtc());
        ensure(!utcSpan().isLocal());
    }

    @Test
    public void testIsUtc()
    {
        ensure(!localSpan().isUtc());
        ensure(utcSpan().isUtc());
    }

    @Test
    public void testStartEndHourOfWeek()
    {
        ensureEqual(hourOfWeek(MONDAY, am(3)), localSpan().startHourOfWeek());
        ensureEqual(hourOfWeek(SATURDAY, pm(11)), localSpan().endHourOfWeek());
    }

    @Test
    public void testZoneId()
    {
        ensureEqual(localSpan().zoneId(), localZone());
        ensureEqual(localSpan().zoneId().getId(), "-07:00");
    }

    /**
     * @return Monday at 3am to Saturday at 11pm in zone -07:00
     */
    @NotNull
    private HourOfWeekSpan localSpan()
    {
        return hourOfWeekSpan(hourOfWeek(MONDAY, am(3)), hourOfWeek(SATURDAY, pm(11)), localZone());
    }

    @NotNull
    private ZoneId localZone()
    {
        return ZoneId.of("-07:00");
    }

    @NotNull
    private ZoneId utc()
    {
        return ZoneId.of("UTC");
    }

    /**
     * @return Monday at 3am to Saturday at 11pm in UTC
     */
    @NotNull
    private HourOfWeekSpan utcSpan()
    {
        return hourOfWeekSpanUtc(hourOfWeek(MONDAY, am(3)), hourOfWeek(SATURDAY, pm(11)));
    }

    /**
     * @return Saturday at 3am, wrapping around to Tuesday 11pm in UTC
     */
    @NotNull
    private HourOfWeekSpan utcSpanWrapping()
    {
        return hourOfWeekSpanUtc(hourOfWeek(SATURDAY, am(3)), hourOfWeek(TUESDAY, pm(11)));
    }
}
