package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.CoreUnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.time.ZoneId;

import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.Hour.am;
import static com.telenav.kivakit.core.time.Hour.pm;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.time.HourOfWeekSpan.hourOfWeekSpan;
import static com.telenav.kivakit.core.time.HourOfWeekSpan.hourOfWeekSpanUtc;

/**
 * Unit test for {@link HourOfWeekSpan}
 *
 * @author jonathanl (shibo)
 */
public class HourOfWeekSpanTest extends CoreUnitTest
{
    @Test
    public void testAsLocal()
    {
        // Monday 3am to Saturday at 11 in zone -07:00
        // should be Monday at 10am to Sunday at 6am
        var utcStart = hourOfWeek(MONDAY, am(10));
        var utcEnd = hourOfWeek(SUNDAY, am(6));
        ensureEqual(localSpan().asUtc(), hourOfWeekSpanUtc(utcStart, utcEnd));
    }

    @Test
    public void testAsUtc()
    {
        // Monday 3am to Saturday midnight in zone -07:00
        // should be Monday at 10am to Sunday at 7am in UTC time
        var utcStart = hourOfWeek(MONDAY, am(10));
        var utcEnd = hourOfWeek(SUNDAY, am(6));
        ensureEqual(localSpan().asUtc(), hourOfWeekSpan(utcStart, utcEnd, utc()));
    }

    @Test
    public void testIsLocal()
    {
        ensure(localSpan().isLocal());
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
     * @return Monday 3am to Saturday 11pm in zone -07:00
     */

    @NotNull
    private HourOfWeekSpan localSpan()
    {
        return new HourOfWeekSpan(hourOfWeek(MONDAY, am(3)), hourOfWeek(SATURDAY, pm(11)), localZone());
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

    @NotNull
    private HourOfWeekSpan utcSpan()
    {
        return hourOfWeekSpanUtc(hourOfWeek(3), hourOfWeek(120));
    }
}
