package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.testing.NoTestRequired;
import com.telenav.kivakit.core.testing.Tested;

import java.time.ZoneId;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;

/**
 * Represents a span of hours of the week in a local timezone. For example, monday at 3pm to tuesday at 1pm in the
 * Pacific timezone (PT).
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class HourOfWeekSpan
{
    @Tested
    public static HourOfWeekSpan hourOfWeekSpan(HourOfWeek startHourOfWeek, HourOfWeek endHourOfWeek, ZoneId timeZone)
    {
        return new HourOfWeekSpan(startHourOfWeek, endHourOfWeek, timeZone);
    }

    @Tested
    public static HourOfWeekSpan hourOfWeekSpanUtc(HourOfWeek startHourOfWeek, HourOfWeek endHourOfWeek)
    {
        return hourOfWeekSpan(startHourOfWeek, endHourOfWeek, TimeZones.utc());
    }

    private HourOfWeek endHourOfWeek;

    private HourOfWeek startHourOfWeek;

    private String timeZone;

    @NoTestRequired
    public HourOfWeekSpan()
    {
    }

    /**
     * Creates and {@link HourOfWeekSpan}, where the start hour of the week does not have to be less than the end hour
     * of the week, and wrapping from sunday to monday is supported. For example, the period from saturday to tuesday
     * would include saturday, sunday, monday and tuesday (inclusive).
     *
     * @param startHourOfWeek The start hour of the week
     * @param endHourOfWeekInclusive The end hour of the week, inclusive
     */
    @NoTestRequired
    protected HourOfWeekSpan(HourOfWeek startHourOfWeek, HourOfWeek endHourOfWeekInclusive, ZoneId timeZone)
    {
        this.startHourOfWeek = ensureNotNull(startHourOfWeek, "Start hour of week is required");
        this.endHourOfWeek = ensureNotNull(endHourOfWeekInclusive, "End hour of week is required");
        this.timeZone = ensureNotNull(timeZone, "Time zone is required").getId();
    }

    /**
     * @return This UTC span of hours in local time
     */
    @Tested
    public HourOfWeekSpan asLocalTime(ZoneId zone)
    {
        if (isUtc())
        {
            return hourOfWeekSpan(startHourOfWeek.asLocalTime(zone), endHourOfWeek.asLocalTime(zone), zone);
        }
        return this;
    }

    /**
     * @return This localtime span of hours in UTC time
     */
    @Tested
    public HourOfWeekSpan asUtc()
    {
        if (!isUtc())
        {
            return hourOfWeekSpan(startHourOfWeek.asUtc(zoneId()), endHourOfWeek.asUtc(zoneId()), ZoneId.of("UTC"));
        }
        return this;
    }

    /**
     * @return The end of this span of hours
     */
    @Tested
    public HourOfWeek endHourOfWeek()
    {
        return endHourOfWeek;
    }

    @Override
    @Tested
    public boolean equals(Object object)
    {
        if (object instanceof HourOfWeekSpan)
        {
            HourOfWeekSpan that = (HourOfWeekSpan) object;
            return Objects.equalPairs(startHourOfWeek(), that.startHourOfWeek(),
                    endHourOfWeek(), that.endHourOfWeek());
        }
        return false;
    }

    @Override
    @Tested
    public int hashCode()
    {
        return Hash.many(startHourOfWeek(), endHourOfWeek());
    }

    /**
     * @param time The time to test
     * @return True if this span of hours (in local time) includes the given local time
     */
    public boolean includes(LocalTime time)
    {
        if (startHourOfWeek.isLessThanOrEqualTo(endHourOfWeek))
        {
            return time.hourOfWeek().isBetweenInclusive(startHourOfWeek, endHourOfWeek);
        }
        else
        {
            return time.hourOfWeek().isBetweenInclusive(startHourOfWeek(), hourOfWeek(0).maximum())
                    || time.hourOfWeek().isBetweenInclusive(hourOfWeek(0), endHourOfWeek());
        }
    }

    /**
     * @param time The time to test
     * @return True if this span of hours (in local time) includes the given UTC time
     */
    public boolean includes(Time time)
    {
        return includes(time.inTimeZone(TimeZones.utc()));
    }

    /**
     * @return True if this span is in localtime
     */
    @Tested
    public boolean isLocal()
    {
        return !isUtc();
    }

    /**
     * @return True if this span of hours is in UTC
     */
    @Tested
    public boolean isUtc()
    {
        return TimeZones.isUtc(zoneId());
    }

    /**
     * The start of this span of hours
     */
    @Tested
    public HourOfWeek startHourOfWeek()
    {
        return startHourOfWeek;
    }

    @Override
    public String toString()
    {
        return Formatter.format("[$ to $]", startHourOfWeek(), endHourOfWeek());
    }

    /**
     * @return The time zone identifier of this span of hours
     */
    @Tested
    public ZoneId zoneId()
    {
        return ZoneId.of(timeZone);
    }
}
