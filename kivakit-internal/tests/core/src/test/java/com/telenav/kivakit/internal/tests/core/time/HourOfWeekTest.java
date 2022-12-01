package com.telenav.kivakit.internal.tests.core.time;

import com.telenav.kivakit.core.time.HourOfWeek;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.time.ZoneId;

import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.WEDNESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.daysOfWeek;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.Hour.militaryHours;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.value.count.Count._100;
import static com.telenav.kivakit.core.value.count.Count._2;

/**
 * Unit test for {@link HourOfWeek}
 *
 * @author jonathanl (shibo)
 */
public class HourOfWeekTest extends CoreUnitTest
{
    @Test
    public void testAsLong()
    {
        ensureEqual(TUESDAY.asHourOfWeek().asUnits(), 24);
        ensureEqual(FRIDAY.asHourOfWeek().asUnits(), 24 * 4);
    }

    @Test
    public void testConversions()
    {
        // For all days of the week,
        daysOfWeek().forEach(dayOfWeek ->
        {
            // and all hours of the day,
            militaryHours().forEach(hourOfDay ->
            {
                // create an HourOfWeek,
                var hourOfWeek = hourOfWeek(dayOfWeek, hourOfDay);

                // and ensure that it can be converted back to each original values.
                ensure(hourOfWeek.dayOfWeek().equals(dayOfWeek));
                ensure(hourOfWeek.hourOfDay().equals(hourOfDay));
            });
        });

        // For all hours of the week from 0 to 167,
        random().rangeExclusive(0, 7 * 24).forEach(at ->
        {
            // create an HourOfWeek for that hour,
            var hourOfWeek = hourOfWeek(at);

            // and ensure that it can be converted to the right day of the week, and hour of the day.
            ensure(hourOfWeek.hourOfDay().asUnits() == at.asInt() % 24);
            ensure(hourOfWeek.dayOfWeek().asIsoOrdinal() == at.asInt() / 24);
        });
    }

    @Test
    public void testCreate()
    {
        // Check valid construction
        ensure(hourOfWeek(5).asUnits() == 5);
        ensure(hourOfWeek(_2).asUnits() == 2);

        _100.loop(() ->
        {
            var range = random().rangeExclusive(0, 7 * 24);
            ensure(range.isExclusive());
            ensure(range.size() <= 168);
            ensure(range.exclusiveMaximum().asInt() <= 168);
            range.forEach(HourOfWeek::hourOfWeek);
            range.forEach(at -> hourOfWeek(at.asInt()));
        });

        // Check invalid construction
        ensureThrows(() -> hourOfWeek(-1));
        ensureThrows(() -> hourOfWeek(7 * 24));
    }

    @Test
    public void testLocalToUtc()
    {
        {
            // Tuesday at 9am in zone +04:00 is Tuesday at 5am UTC.
            var utc = hourOfWeek(TUESDAY, militaryHour(9))
                    .asUtc(ZoneId.of("+04:00"));
            ensureEqual(utc.dayOfWeek(), TUESDAY);
            ensureEqual(utc.hourOfDay(), militaryHour(5));
        }

        {
            // Tuesday at 9pm in zone -04:00 is Wednesday at 1am UTC.
            var utc = hourOfWeek(TUESDAY, militaryHour(12 + 9))
                    .asUtc(ZoneId.of("-04:00"));
            ensureEqual(utc.dayOfWeek(), WEDNESDAY);
            ensureEqual(utc.hourOfDay(), militaryHour(1));
        }
    }

    @Test
    public void testLocalToUtcWrapAround()
    {
        {
            // Wednesday at 9am in zone +10:00 is Tuesday at 11pm UTC.
            var utc = hourOfWeek(WEDNESDAY, militaryHour(9))
                    .asUtc(ZoneId.of("+10:00"));
            ensureEqual(utc.dayOfWeek(), TUESDAY);
            ensureEqual(utc.hourOfDay(), militaryHour(12 + 11));
        }

        {
            // Tuesday at 6pm in zone -10:00 is Wednesday at 4am UTC.
            var utc = hourOfWeek(TUESDAY, militaryHour(12 + 6))
                    .asUtc(ZoneId.of("-10:00"));
            ensureEqual(utc.dayOfWeek(), WEDNESDAY);
            ensureEqual(utc.hourOfDay(), militaryHour(4));
        }
    }

    @Test
    public void testMaximum()
    {
        ensure(hourOfWeek(0).maximum().asUnits() == 167);
    }

    @Test
    public void testMinimum()
    {
        ensure(hourOfWeek(0).minimum().asUnits() == 0);
    }

    @Test
    public void testMinus()
    {
        ensureEqual(hourOfWeek(7).minusUnits(24).dayOfWeek(), SUNDAY);
        ensureEqual(hourOfWeek(24 * 6).minusUnits(7).dayOfWeek(), SATURDAY);
        ensureEqual(hourOfWeek(24 * 6).minusUnits(24 + 7).dayOfWeek(), FRIDAY);
    }

    @Test
    public void testPlus()
    {
        ensureEqual(hourOfWeek(7).plusUnits(24).dayOfWeek(), TUESDAY);
        ensureEqual(hourOfWeek(24 * 6).plusUnits(7).dayOfWeek(), SUNDAY);
        ensureEqual(hourOfWeek(24 * 6).plusUnits(24 + 7).dayOfWeek(), MONDAY);
    }

    @Test
    public void testUtcToLocal()
    {
        {
            //  Tuesday at 9am in time zone -07:00, is TUESDAY at 2am,
            var local = hourOfWeek(TUESDAY, militaryHour(9))
                    .asLocalTime(ZoneId.of("-07:00"));
            ensureEqual(local.dayOfWeek(), TUESDAY);
            ensureEqual(local.hourOfDay(), militaryHour(2));
        }

        {
            // Tuesday at 9am in time zone -10:00, is Monday at 11pm.
            var local = hourOfWeek(TUESDAY, militaryHour(9))
                    .asLocalTime(ZoneId.of("-10:00"));
            ensureEqual(local.dayOfWeek(), MONDAY);
            ensureEqual(local.hourOfDay(), militaryHour(12 + 11));
        }
    }

    @Test
    public void testUtcToLocalWrapAround()
    {
        {
            // Monday at 9am in time zone -10:00, is Sunday at 11pm.
            var local = hourOfWeek(MONDAY, militaryHour(9))
                    .asLocalTime(ZoneId.of("-10:00"));
            ensureEqual(local.dayOfWeek(), SUNDAY);
            ensureEqual(local.hourOfDay(), militaryHour(23));
        }

        {
            // Sunday at 9pm in time zone 6:00, is MONDAY at 3am.
            var local = hourOfWeek(SUNDAY, militaryHour(12 + 9))
                    .asLocalTime(ZoneId.of("+06:00"));
            ensureEqual(local.dayOfWeek(), MONDAY);
            ensureEqual(local.hourOfDay(), militaryHour(3));
        }
    }
}
