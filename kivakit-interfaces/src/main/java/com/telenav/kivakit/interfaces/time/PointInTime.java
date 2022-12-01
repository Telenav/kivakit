package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.time.Instant;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

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
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface PointInTime<Time extends PointInTime<Time, Duration>, Duration extends LengthOfTime<Duration>> extends
        Comparable<PointInTime<?, ?>>,
        TimeMeasurement
{
    /**
     * Returns a Java {@link Instant} for this time value
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
     * Returns true if this time value is after the given value
     */
    default boolean isAfter(Time that)
    {
        return isGreaterThan(that);
    }

    /**
     * Returns true if this time value is at or after the given value
     */
    default boolean isAtOrAfter(Time that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * Returns true if this time value is at or before the given value
     */
    default boolean isAtOrBefore(Time that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * Returns true if this time value is before the given value
     */
    default boolean isBefore(Time that)
    {
        return isLessThan(that);
    }

    /**
     * Returns true if this time value is after the given value
     */
    default boolean isGreaterThan(Time that)
    {
        return nanoseconds().isGreaterThan(that.nanoseconds());
    }

    /**
     * Returns true if this time value is at or after the given value
     */
    default boolean isGreaterThanOrEqualTo(Time that)
    {
        return nanoseconds().isGreaterThanOrEqualTo(that.nanoseconds());
    }

    /**
     * Returns true if this time value is before the given value
     */
    default boolean isLessThan(Time that)
    {
        return nanoseconds().isLessThan(that.nanoseconds());
    }

    /**
     * Returns true if this time value is at or before the given value
     */
    default boolean isLessThanOrEqualTo(Time that)
    {
        return nanoseconds().isLessThanOrEqualTo(that.nanoseconds());
    }

    /**
     * Returns true if this is a negative point in time
     */
    default boolean isNegative()
    {
        return nanoseconds().isNegative();
    }

    /**
     * Returns true if this is time zero
     */
    @Override
    default boolean isZero()
    {
        return nanoseconds().isZero();
    }

    /**
     * Returns the maximum point in time
     */
    Time maximum();

    /**
     * Returns the largest of this point in time and the given point in time
     */
    @SuppressWarnings("unchecked")
    default Time maximum(Time that)
    {
        return isAfter(that) ? (Time) this : that;
    }

    /**
     * Returns the minimum point in time
     */
    Time minimum();

    /**
     * Returns the smallest of this point in time and the given point in time
     */
    @SuppressWarnings("unchecked")
    default Time minimum(Time that)
    {
        return isBefore(that) ? (Time) this : that;
    }

    /**
     * Returns this point in time minus the given length of time
     */
    default Time minus(Duration duration)
    {
        return newTime(nanoseconds().minus(duration.nanoseconds()));
    }

    /**
     * Returns this point in time minus the given point in time as a duration
     */
    default Duration minus(Time that)
    {
        return newDuration(nanoseconds().minus(that.nanoseconds()));
    }

    /**
     * Returns the nearest point in time of the given unit
     */
    default Time nearest(Duration unit)
    {
        return plus(unit.dividedBy(2)).roundDown(unit);
    }

    /**
     * Creates an instance of the class implementing LengthOfTime
     */
    Duration newDuration(Nanoseconds nanoseconds);

    /**
     * Creates an instance of the class implementing PointInTime
     */
    Time newTime(Nanoseconds nanoseconds);

    /**
     * Returns this point in time plus the given duration
     */
    default Time plus(Duration that)
    {
        return newTime(nanoseconds().plus(that.nanoseconds()));
    }

    /**
     * Returns this point in time rounded down to the closest unit
     */
    default Time roundDown(Duration unit)
    {
        return newTime(nanoseconds().roundDown(unit.nanoseconds()));
    }

    /**
     * Returns this point in time rounded up to the closest unit
     */
    default Time roundUp(Duration unit)
    {
        return roundDown(unit).plus(unit);
    }
}
