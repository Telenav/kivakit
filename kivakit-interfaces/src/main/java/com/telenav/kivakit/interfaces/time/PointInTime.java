package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.string.Stringable;

import java.time.Instant;

/**
 * Interface to an object having a length of time, measured in milliseconds.
 *
 * <p><b>Measurement</b></p>
 *
 * <ul>
 *     <li>{@link #nanoseconds()}</li>
 *     <li>{@link #milliseconds()}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <p>
 * A length of time can be converted to specific time units by calling one of the following methods:
 * <ul>
 *     <li>{@link #asMicroseconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 *     <li>{@link #asJavaInstant()}</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #plus(LengthOfTime)}</li>
 *     <li>{@link #minus(LengthOfTime)}</li>
 *     <li>{@link #minus(PointInTime)}</li>
 *     <li>{@link #roundUp(LengthOfTime)}</li>
 *     <li>{@link #roundDown(LengthOfTime)}</li>
 *     <li>{@link #nearest(LengthOfTime)}</li>
 * </ul>
 *
 * <p><b>Minima and Maxima</b></p>
 *
 * <ul>
 *     <li>{@link #minimum()}</li>
 *     <li>{@link #maximum()}</li>
 *     <li>{@link #minimum(PointInTime)}</li>
 *     <li>{@link #maximum(PointInTime)}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(PointInTime)}</li>
 *     <li>{@link #isBefore(PointInTime)}</li>
 *     <li>{@link #isAfter(PointInTime)}</li>
 *     <li>{@link #isAtOrBefore(PointInTime)}</li>
 *     <li>{@link #isAtOrAfter(PointInTime)}</li>
 *     <li>{@link #isZero()}</li>
 *     <li>{@link #isNonZero()}</li>
 *     <li>{@link #isAfter(PointInTime)}</li>
 *     <li>{@link #isAtOrAfter(PointInTime)}</li>
 *     <li>{@link #isBefore(PointInTime)}</li>
 *     <li>{@link #isAtOrBefore(PointInTime)}</li>
 * </ul>
 *
 * <p><b>Implementation</b></p>
 *
 * <ul>
 *     <li>{@link #newTime(Nanoseconds)}</li>
 *     <li>{@link #newDuration(Nanoseconds)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public interface PointInTime<Time extends PointInTime<Time, Duration>, Duration extends LengthOfTime<Duration>> extends
        Comparable<PointInTime<?, ?>>,
        Stringable,
        TimeMeasurement
{
    /**
     * @return A Java {@link Instant} for this time value
     */
    default Instant asJavaInstant()
    {
        return Instant.ofEpochMilli(milliseconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(PointInTime<?, ?> that)
    {
        return nanoseconds().compareTo(that.nanoseconds());
    }

    /**
     * @return True if this time value is after the given value
     */
    default boolean isAfter(Time that)
    {
        return isGreaterThan(that);
    }

    /**
     * @return True if this time value is at or after the given value
     */
    default boolean isAtOrAfter(Time that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * @return True if this time value is at or before the given value
     */
    default boolean isAtOrBefore(Time that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * @return True if this time value is before the given value
     */
    default boolean isBefore(Time that)
    {
        return isLessThan(that);
    }

    /**
     * @return True if this time value is after the given value
     */
    default boolean isGreaterThan(Time that)
    {
        return nanoseconds().isGreaterThan(that.nanoseconds());
    }

    /**
     * @return True if this time value is at or after the given value
     */
    default boolean isGreaterThanOrEqualTo(Time that)
    {
        return nanoseconds().isGreaterThanOrEqualTo(that.nanoseconds());
    }

    /**
     * @return True if this time value is before the given value
     */
    default boolean isLessThan(Time that)
    {
        return nanoseconds().isLessThan(that.nanoseconds());
    }

    /**
     * @return True if this time value is at or before the given value
     */
    default boolean isLessThanOrEqualTo(Time that)
    {
        return nanoseconds().isLessThanOrEqualTo(that.nanoseconds());
    }

    default boolean isNegative()
    {
        return nanoseconds().isNegative();
    }

    default boolean isZero()
    {
        return nanoseconds().isZero();
    }

    /**
     * @return The maximum point in time
     */
    Time maximum();

    /**
     * @return The larger of this point in time and the given point in time
     */
    @SuppressWarnings("unchecked")
    default Time maximum(Time that)
    {
        return isAfter(that) ? (Time) this : that;
    }

    /**
     * @return The minimum point in time
     */
    Time minimum();

    /**
     * @return The smaller of this point in time and the given point in time
     */
    @SuppressWarnings("unchecked")
    default Time minimum(Time that)
    {
        return isBefore(that) ? (Time) this : that;
    }

    /**
     * @return This point in time minus the given length of time
     */
    default Time minus(Duration duration)
    {
        return newTime(nanoseconds().minus(duration.nanoseconds()));
    }

    /**
     * @return This point in time minus the given point in time as a duration
     */
    default Duration minus(Time that)
    {
        return newDuration(nanoseconds().minus(that.nanoseconds()));
    }

    /**
     * @return The nearest point in time of the given unit
     */
    default Time nearest(Duration unit)
    {
        return plus(unit.dividedBy(2)).roundDown(unit);
    }

    /**
     * @return An instance of the class implementing LengthOfTime
     */
    Duration newDuration(Nanoseconds nanoseconds);

    /**
     * @return An instance of the class implementing PointInTime
     */
    Time newTime(Nanoseconds nanoseconds);

    /**
     * @return This point in time plus the given duration
     */
    default Time plus(Duration that)
    {
        return newTime(nanoseconds().plus(that.nanoseconds()));
    }

    /**
     * @return This point in time rounded down to the closest unit
     */
    default Time roundDown(Duration unit)
    {
        return newTime(nanoseconds().roundDown(unit.nanoseconds()));
    }

    /**
     * @return This point in time rounded up to the closest unit
     */
    default Time roundUp(Duration unit)
    {
        return roundDown(unit).plus(unit);
    }
}
