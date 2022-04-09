package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.value.count.BaseCount;

import java.time.Instant;
import java.time.ZoneId;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;
import static com.telenav.kivakit.core.time.Duration.milliseconds;
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

    /**
     * Calculates the amount of time that has elapsed since this <code>Time</code> value.
     *
     * @return the amount of time that has elapsed since this <code>Time</code> value
     */
    public Duration elapsedSince()
    {
        return elapsedSince(Time.now());
    }

    /**
     * Subtract time from this and returns the difference as a <code>Duration</code> object.
     *
     * @param that The time to subtract
     * @return The <code>Duration</code> between this and that time
     */
    public Duration elapsedSince(BaseTime<?> that)
    {
        // If this time is after the given time,
        if (isAfter(that))
        {
            // then we can subtract to get the duration.
            return milliseconds(asMilliseconds() - that.asUtc().asMilliseconds());
        }

        return ZERO_DURATION;
    }

    public LocalTime inTimeZone(ZoneId zone)
    {
        return LocalTime.localTime(ensureNotNull(zone), this);
    }

    /**
     * @return True if this time value is after the given value
     */
    public boolean isAfter(BaseTime<?> that)
    {
        return isGreaterThan(that);
    }

    /**
     * @return True if this time value is at or after the given value
     */
    public boolean isAtOrAfter(BaseTime<?> that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * @return True if this time value is at or before the given value
     */
    public boolean isAtOrBefore(BaseTime<?> that)
    {
        return isLessThan(that);
    }

    /**
     * @return True if this time value is before the given value
     */
    public boolean isBefore(BaseTime<?> that)
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

    /**
     * @return True if this time value is newer than the given {@link Duration}
     */
    public boolean isNewerThan(Duration duration)
    {
        return elapsedSince().isLessThan(duration);
    }

    /**
     * @return True if this time value is newer than the given time value
     */
    public boolean isNewerThan(BaseTime<?> that)
    {
        return isGreaterThan(that);
    }

    /**
     * @return True if this time value is newer than or equal to the given duration
     */
    public boolean isNewerThanOrEqual(Duration duration)
    {
        return elapsedSince().isLessThanOrEqualTo(duration);
    }

    /**
     * @return True if this time value is newer than or equal to the given time value
     */
    public boolean isNewerThanOrEqualTo(BaseTime<?> that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * True if this time is now older than the given duration.
     *
     * @param duration Amount of time to be considered old
     * @return True if the time that has elapsed since this time is greater than the given duration
     */
    public boolean isOlderThan(Duration duration)
    {
        return elapsedSince().isGreaterThan(duration);
    }

    public boolean isOlderThan(BaseTime<?> that)
    {
        return isLessThan(that);
    }

    public boolean isOlderThanOrEqual(Duration duration)
    {
        return elapsedSince().isGreaterThanOrEqualTo(duration);
    }

    public boolean isOlderThanOrEqualTo(BaseTime<?> that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * @return The amount of time left until the given amount of time has elapsed
     */
    public Duration leftUntil(Duration elapsed)
    {
        return elapsed.minus(elapsedSince());
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

    public Duration until(Time that)
    {
        return that.elapsedSince(this);
    }

    /**
     * Retrieves the <code>Duration</code> from now to this <code>Time</code> value. If this
     * <code>Time</code> value is in the past, then the <code>Duration</code> returned will be
     * negative. Otherwise, it will be the number of milliseconds from now to this <code>Time</code> .
     *
     * @return the <code>Duration</code> from now to this <code>Time</code> value
     */
    public Duration untilNow()
    {
        return until(Time.now());
    }

    protected long asUnits()
    {
        return asLong() / millisecondsPerUnit();
    }
}
