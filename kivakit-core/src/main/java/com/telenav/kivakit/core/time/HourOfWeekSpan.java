package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.test.Tested;

import java.time.ZoneId;

/**
 * Represents a span of hours of the week in a local timezone. For example, monday at 3pm to tuesday at 1pm in the
 * Pacific timezone (PT).
 *
 * @author jonathanl (shibo)
 */
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

    public HourOfWeekSpan()
    {
    }

    @Tested
    protected HourOfWeekSpan(HourOfWeek startHourOfWeek, HourOfWeek endHourOfWeek, ZoneId timeZone)
    {
        this.endHourOfWeek = endHourOfWeek;
        this.startHourOfWeek = startHourOfWeek;
        this.timeZone = timeZone.getId();
    }

    /**
     * @return This UTC span of hours in local time
     */
    public HourOfWeekSpan asLocalTime()
    {
        if (isUtc())
        {
            return new HourOfWeekSpan(startHourOfWeek.asLocalTime(zoneId()), endHourOfWeek.asLocalTime(zoneId()), zoneId());
        }
        return this;
    }

    /**
     * @return This localtime span of hours in UTC time
     */
    public HourOfWeekSpan asUtc()
    {
        if (!isUtc())
        {
            return new HourOfWeekSpan(startHourOfWeek.asUtc(zoneId()), endHourOfWeek.asUtc(zoneId()), ZoneId.of("UTC"));
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
    public boolean equals(final Object object)
    {
        if (object instanceof HourOfWeekSpan)
        {
            HourOfWeekSpan that = (HourOfWeekSpan) object;
            return Objects.equalPairs(startHourOfWeek(), that.startHourOfWeek(), endHourOfWeek(), that.endHourOfWeek());
        }
        return false;
    }

    @Override
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
        return HourOfWeek.hourOfWeek(time.hourOfWeek())
                .isBetweenInclusive(startHourOfWeek, endHourOfWeek);
    }

    /**
     * @param time The time to test
     * @return True if this span of hours (in local time) includes the given UTC time
     */
    public boolean includes(Time time)
    {
        return includes(time.localTime(timeZone));
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
