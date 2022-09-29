package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.kivakit.interfaces.time.PointInTime;

import java.util.Objects;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;

/**
 * Base class for values representing a {@link PointInTime}:
 *
 * <ul>
 *     <li>{@link Time}</li>
 *     <li>{@link LocalTime}</li>
 *     <li>{@link Year}</li>
 *     <li>{@link Week}</li>
 *     <li>{@link Day}</li>
 *     <li>{@link DayOfWeek}</li>
 *     <li>{@link Hour}</li>
 *     <li>{@link HourOfWeek}</li>
 *     <li>{@link Minute}</li>
 *     <li>{@link Second}</li>
 * </ul>
 *
 * <p><b>Measurement</b></p>
 *
 * <ul>
 *     <li>{@link #nanoseconds()}</li>
 *     <li>{@link #milliseconds()}</li>
 * </ul>
 *
 * <p><b>Units</b></p>
 *
 * <p>
 * Each time value is represented as a number of nanoseconds which can be converted to/from a number of units.
 * The number of nanoseconds per unit is defined by subclasses by overriding {@link #nanosecondsPerUnit()},
 * and units can be retrieved by subclasses with {@link #asUnits()}. For example, {@link Hour#asUnits()}
 * will return the number of hours, based on the number of nanoseconds.
 * </p>
 *
 * <ul>
 *     <li>{@link #asUnits()}</li>
 *     <li>{@link #nanosecondsPerUnit()}</li>
 *     <li>{@link #nanosecondsToUnits(Nanoseconds)}</li>
 *     <li>{@link #unitsToNanoseconds(double)}</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #decremented()}</li>
 *     <li>{@link #incremented()}</li>
 *     <li>{@link #plus(LengthOfTime)}</li>
 *     <li>{@link #plusUnits(double)}</li>
 *     <li>{@link #minus(LengthOfTime)}</li>
 *     <li>{@link #minusUnits(double)}</li>
 *     <li>{@link #next()}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(PointInTime)}</li>
 *     <li>{@link #longComparable()}</li>
 *     <li>{@link #isBetweenExclusive(BaseTime, BaseTime)}</li>
 *     <li>{@link #isBetweenInclusive(BaseTime, BaseTime)}</li>
 *     <li>{@link #isBefore(PointInTime)}</li>
 *     <li>{@link #isAfter(PointInTime)}</li>
 *     <li>{@link #isAtOrBefore(PointInTime)}</li>
 *     <li>{@link #isAtOrAfter(PointInTime)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <p>
 * A length of time can be converted to specific time units by calling one of the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asJavaInstant()}</li>
 *     <li>{@link #asMicroseconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asNanoseconds()}</li>
 *     <li>{@link #asPreciseUnits()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asString(Format)}</li>
 *     <li>{@link #asUnits()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #minus(PointInTime)}</li>
 *     <li>{@link #minus(LengthOfTime)}</li>
 *     <li>{@link #minusUnits(double)}</li>
 *     <li>{@link #plus(LengthOfTime)}</li>
 *     <li>{@link #plusUnits(double)}</li>
 *     <li>{@link #next()}</li>
 *     <li>{@link #nearest(LengthOfTime)}</li>
 *     <li>{@link #roundUp(LengthOfTime)}</li>
 *     <li>{@link #roundDown(LengthOfTime)}</li>
 * </ul>
 *
 * <p><b>Implementation</b></p>
 *
 * <ul>
 *     <li>{@link #newDuration(Nanoseconds)}</li>
 *     <li>{@link #newTime(Nanoseconds)}</li>
 *     <li>{@link #onNewTime(Nanoseconds)}</li>
 * </ul>
 *
 * @see com.telenav.kivakit.core.time.Time
 * @see LocalTime
 * @see Year
 * @see Week
 * @see Day
 * @see DayOfWeek
 * @see Hour
 * @see HourOfWeek
 * @see Minute
 * @see Second
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public abstract class BaseTime<TimeType extends BaseTime<TimeType>> implements
        PointInTime<TimeType, Duration>,
        StringFormattable
{
    @ApiQuality(stability = STABLE,
                testing = TESTING_NOT_NEEDED,
                documentation = FULLY_DOCUMENTED)
    public enum Topology
    {
        LINEAR,
        CYCLIC
    }

    /** The number of nanoseconds */
    private Nanoseconds nanoseconds;

    public BaseTime()
    {
    }

    public BaseTime(Nanoseconds nanoseconds)
    {
        this.nanoseconds = nanoseconds;
    }

    /**
     * @return This point in time in units
     */
    public double asPreciseUnits()
    {
        return nanosecondsToUnits(nanoseconds());
    }

    @Override
    public String asString(Format format)
    {
        return toString();
    }

    /**
     * Returns this time in some unit defined by a subclass
     */
    public int asUnits()
    {
        return (int) asPreciseUnits();
    }

    /**
     * @return This time minus one unit
     */
    public TimeType decremented()
    {
        return minusUnits(1);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof BaseTime)
        {
            var that = (BaseTime<?>) object;
            return this.nanoseconds().equals(that.nanoseconds());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(nanoseconds());
    }

    /**
     * @return This time plus one unit
     */
    public TimeType incremented()
    {
        return plusUnits(1);
    }

    /**
     * Returns true if this time is between the given times, exclusive
     *
     * @param minimum The minimum time
     * @param maximum The maximum time
     */
    public boolean isBetweenExclusive(TimeType minimum, TimeType maximum)
    {
        return nanoseconds().isGreaterThanOrEqualTo(minimum.nanoseconds())
                && nanoseconds().isLessThan(maximum.nanoseconds());
    }

    /**
     * Returns true if this time is between the given times, inclusive
     *
     * @param minimum The minimum time
     * @param maximum The maximum time
     */
    public boolean isBetweenInclusive(TimeType minimum, TimeType maximum)
    {
        return nanoseconds().isGreaterThanOrEqualTo(minimum.nanoseconds())
                && nanoseconds().isLessThanOrEqualTo(maximum.nanoseconds());
    }

    /**
     * @return This time minus the given number of units
     */
    public TimeType minusUnits(double units)
    {
        return minus(Duration.nanoseconds(unitsToNanoseconds(units)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Nanoseconds nanoseconds()
    {
        return nanoseconds;
    }

    /**
     * Returns the number of nanoseconds per unit of time
     */
    public abstract Nanoseconds nanosecondsPerUnit();

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration newDuration(Nanoseconds nanoseconds)
    {
        return Duration.nanoseconds(nanoseconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TimeType newTime(Nanoseconds nanoseconds)
    {
        return onNewTime(inRange(nanoseconds));
    }

    /**
     * @return This time plus one unit
     */
    public TimeType next()
    {
        return plusUnits(1);
    }

    /**
     * Called by {@link #newTime(Nanoseconds)} to create a time object in the subclass
     */
    public abstract TimeType onNewTime(Nanoseconds nanoseconds);

    /**
     * @return This time plus the given number of units
     */
    public TimeType plusUnits(double units)
    {
        return newTime(nanoseconds().plus(unitsToNanoseconds(units)));
    }

    @Override
    public String toString()
    {
        return String.valueOf(asUnits());
    }

    /**
     * <b>Not public API</b>
     */
    protected Nanoseconds inRange(Nanoseconds nanoseconds)
    {
        if (topology() == CYCLIC)
        {
            var units = nanosecondsToUnits(nanoseconds);
            var minimumUnits = minimum().asUnits();
            var maximumUnits = maximum().asUnits();

            var range = maximumUnits - minimumUnits + 1;
            ensure(range > 0);

            if (units < minimumUnits)
            {
                var distanceFromMinimum = minimumUnits - units;
                return unitsToNanoseconds(minimumUnits + (maximumUnits + 1 - distanceFromMinimum) % range);
            }

            if (units > maximumUnits)
            {
                return unitsToNanoseconds(minimumUnits + (units % range));
            }
        }

        return nanoseconds;
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Converts the given number of nanoseconds to units
     */
    protected double nanosecondsToUnits(Nanoseconds nanoseconds)
    {
        return nanoseconds.dividedBy(nanosecondsPerUnit());
    }

    /**
     * @return The kind of time this is, either {@link Topology#LINEAR} or {@link Topology#CYCLIC}
     */
    protected abstract Topology topology();

    /**
     * <b>Not public API</b>
     * <p>
     * Converts the given number of units to nanoseconds
     */
    protected Nanoseconds unitsToNanoseconds(double units)
    {
        return nanosecondsPerUnit().times(units);
    }
}
