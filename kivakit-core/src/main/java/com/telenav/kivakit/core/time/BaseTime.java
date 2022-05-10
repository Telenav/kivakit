package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.value.count.BaseCount;

import java.time.Instant;
import java.time.ZoneId;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.LocalTime.localTimeZone;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;

/**
 * Base class for time values:
 *
 * <ul>
 *     <li>{@link Year}</li>
 *     <li>{@link Week}</li>
 *     <li>{@link Day}</li>
 *     <li>{@link Hour}</li>
 *     <li>{@link Minute}</li>
 *     <li>{@link Second}</li>
 * </ul>
 *
 * <p><b>Milliseconds and Units</b></p>
 *
 * <p>
 * Each time value is represented as a count of milliseconds by the base class {@link BaseCount},
 * which provides basic arithmetic operations. The number of milliseconds per unit is defined by
 * subclasses with {@link #millisecondsPerUnit()}, and units can be retrieved by subclasses
 * with {@link #asUnits()}. For example, {@link Hour#asUnits()} will return the number of hours,
 * based on the number of milliseconds.
 * </p>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 * </ul>
 *
 * @see Year
 * @see Week
 * @see Day
 * @see Hour
 * @see Second
 */
@SuppressWarnings("unused")
public abstract class BaseTime<T extends BaseTime<T>> extends BaseCount<T>
{
    public BaseTime()
    {
    }

    public BaseTime(long count)
    {
        super(count);
    }

    /**
     * @return The number of days for this time value
     */
    public double asDays()
    {
        return asHours() / 24;
    }

    /**
     * @return The number of hours for this time value
     */
    public double asHours()
    {
        return asMinutes() / 60;
    }

    /**
     * @return A Java {@link Instant} for this time value
     */
    public Instant asInstant()
    {
        return Instant.ofEpochMilli(asMilliseconds());
    }

    /**
     * @return The number of milliseconds since the start of the UNIX epoch on January 1, 1970
     */
    public long asMilliseconds()
    {
        return asLong();
    }

    /**
     * @return The number of minutes for this time value
     */
    public double asMinutes()
    {
        return asSeconds() / 60;
    }

    /**
     * @return The number of seconds for this time value
     */
    public double asSeconds()
    {
        return asMilliseconds() / 1000.0;
    }

    public Time asUtc()
    {
        return inTimeZone(utcTimeZone());
    }

    /**
     * @return The number of weeks for this time value
     */
    public double asWeeks()
    {
        return asDays() / 7;
    }

    public LocalTime inTimeZone(ZoneId zone)
    {
        return LocalTime.localTime(ensureNotNull(zone), this);
    }

    /**
     * @return True if this time value is after the given value
     */
    public boolean isAfter(T that)
    {
        return isGreaterThan(that);
    }

    /**
     * @return True if this time value is at or after the given value
     */
    public boolean isAtOrAfter(T that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * @return True if this time value is at or before the given value
     */
    public boolean isAtOrBefore(T that)
    {
        return isLessThan(that);
    }

    /**
     * @return True if this time value is before the given value
     */
    public boolean isBefore(T that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * Returns true if this time has a time zone
     */
    public boolean isLocal()
    {
        return false;
    }

    public LocalTime localTime()
    {
        return inTimeZone(localTimeZone());
    }

    @Override
    @SuppressWarnings("unchecked")
    public T maximum(T that)
    {
        return isAfter(that) ? (T) this : that;
    }

    /**
     * Returns the number of milliseconds per unit of time
     */
    public long millisecondsPerUnit()
    {
        return unsupported();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T minimum(T that)
    {
        return isBefore(that) ? (T) this : that;
    }

    /**
     * Subtracts the given <code>Duration</code> from this <code>Time</code> object, moving the time into the past.
     *
     * @param duration the <code>Duration</code> to subtract
     * @return this duration of time
     */
    public T minus(Duration duration)
    {
        return newInstance(asMilliseconds() - duration.milliseconds());
    }

    public T nearest(Duration unit)
    {
        return plus(unit.dividedBy(2)).roundDown(unit);
    }

    /**
     * Adds the given <code>Duration</code> to this <code>Time</code> object, moving the time into the future.
     *
     * @param duration the <code>Duration</code> to add
     * @return this <code>Time</code> + <code>Duration</code>
     */
    public T plus(Duration duration)
    {
        return plus(duration.milliseconds());
    }

    public T roundDown(Duration unit)
    {
        return newInstance(asMilliseconds() / unit.milliseconds() * unit.milliseconds());
    }

    public T roundUp(Duration unit)
    {
        return roundDown(unit).plus(unit);
    }

    public ZoneId timeZone()
    {
        return utcTimeZone();
    }

    protected long asUnits()
    {
        return asLong() / millisecondsPerUnit();
    }
}
