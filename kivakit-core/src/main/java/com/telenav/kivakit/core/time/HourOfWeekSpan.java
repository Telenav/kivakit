package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.string.Formatter;

import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;

/**
 * Represents a span of hours of the week in a local timezone. For example, monday at 3pm to tuesday at 1pm in the
 * Pacific timezone (PT).
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #hourOfWeekSpan(HourOfWeek, HourOfWeek, ZoneId)}</li>
 *     <li>{@link #hourOfWeekSpanUtc(HourOfWeek, HourOfWeek)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #startHourOfWeek()}</li>
 *     <li>{@link #endHourOfWeek}()</li>
 * </ul>
 *
 * <p><b>Tests</b></p>
 *
 * <ul>
 *     <li>{@link #includes(Time)}</li>
 *     <li>{@link #includes(LocalTime)}</li>
 *     <li>{@link #isLocal()}</li>
 *     <li>{@link #isUtc()}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asUtc()}</li>
 *     <li>{@link #asLocalTime(ZoneId)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class HourOfWeekSpan
{
    /**
     * Returns an hour of the week span from the given start hour to the given end hour, in the given timezone
     *
     * @param startHourOfWeek The start hour
     * @param endHourOfWeek The end hour
     * @param timeZone The timezone
     * @return The hour of the week span
     */
    public static HourOfWeekSpan hourOfWeekSpan(HourOfWeek startHourOfWeek, HourOfWeek endHourOfWeek, ZoneId timeZone)
    {
        return new HourOfWeekSpan(startHourOfWeek, endHourOfWeek, timeZone);
    }

    /**
     * Returns an hour of the week span from the given start hour to the given end hour, in the UTC timezone
     *
     * @param startHourOfWeek The start hour
     * @param endHourOfWeek The end hour
     * @return The hour of the week span
     */
    public static HourOfWeekSpan hourOfWeekSpanUtc(HourOfWeek startHourOfWeek, HourOfWeek endHourOfWeek)
    {
        return hourOfWeekSpan(startHourOfWeek, endHourOfWeek, TimeZones.utc());
    }

    /** The end hour */
    private HourOfWeek endHourOfWeek;

    /** The start hour */
    private HourOfWeek startHourOfWeek;

    /** The timezone */
    private String timeZone;

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
    protected HourOfWeekSpan(HourOfWeek startHourOfWeek, HourOfWeek endHourOfWeekInclusive, ZoneId timeZone)
    {
        this.startHourOfWeek = ensureNotNull(startHourOfWeek, "Start hour of week is required");
        this.endHourOfWeek = ensureNotNull(endHourOfWeekInclusive, "End hour of week is required");
        this.timeZone = ensureNotNull(timeZone, "Time zone is required").getId();
    }

    /**
     * Returns this UTC span of hours in local time
     */
    public HourOfWeekSpan asLocalTime(ZoneId zone)
    {
        if (isUtc())
        {
            return hourOfWeekSpan(startHourOfWeek.asLocalTime(zone), endHourOfWeek.asLocalTime(zone), zone);
        }
        return this;
    }

    /**
     * Returns this localtime span of hours in UTC time
     */
    public HourOfWeekSpan asUtc()
    {
        if (!isUtc())
        {
            return hourOfWeekSpan(startHourOfWeek.asUtc(zoneId()), endHourOfWeek.asUtc(zoneId()), ZoneId.of("UTC"));
        }
        return this;
    }

    /**
     * Returns the end of this span of hours
     */
    public HourOfWeek endHourOfWeek()
    {
        return endHourOfWeek;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof HourOfWeekSpan)
        {
            HourOfWeekSpan that = (HourOfWeekSpan) object;
            return Objects.areEqualPairs(startHourOfWeek(), that.startHourOfWeek(),
                    endHourOfWeek(), that.endHourOfWeek());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.hashMany(startHourOfWeek(), endHourOfWeek());
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
     * Returns true if this span is in localtime
     */
    public boolean isLocal()
    {
        return !isUtc();
    }

    /**
     * Returns true if this span of hours is in UTC
     */
    public boolean isUtc()
    {
        return TimeZones.isUtc(zoneId());
    }

    /**
     * The start of this span of hours
     */
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
     * Returns the time zone identifier of this span of hours
     */
    public ZoneId zoneId()
    {
        return ZoneId.of(timeZone);
    }
}
