package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.kivakit.interfaces.time.PointInTime;

import java.util.Objects;

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
 * <p><b>Conversion</b></p>
 *
 * <p>
 * A length of time can be converted to specific time units by calling one of the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #asUnits()}</li>
 *     <li>{@link #asJavaInstant()}</li>
 *     <li>{@link #asNanoseconds()}</li>
 *     <li>{@link #asMicroseconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
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
 * </ul>
 *
 * @see Time
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
public abstract class BaseTime<T extends BaseTime<T>> implements
        PointInTime<T, Duration>,
        StringFormattable
{
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

    public int asUnits()
    {
        return (int) asPreciseUnits();
    }

    /**
     * @return This time minus one unit
     */
    public T decremented()
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
    public T incremented()
    {
        return plusUnits(1);
    }

    public boolean isBetweenExclusive(T minimum, T maximum)
    {
        return nanoseconds().isGreaterThanOrEqualTo(minimum.nanoseconds())
                && nanoseconds().isLessThan(maximum.nanoseconds());
    }

    public boolean isBetweenInclusive(T minimum, T maximum)
    {
        return nanoseconds().isGreaterThanOrEqualTo(minimum.nanoseconds())
                && nanoseconds().isLessThanOrEqualTo(maximum.nanoseconds());
    }

    /**
     * @return This time minus the given number of units
     */
    public T minusUnits(double units)
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
     * Returns the number of milliseconds per unit of time
     */
    public abstract Nanoseconds nanosecondsPerUnit();

    @Override
    public Duration newDuration(Nanoseconds nanoseconds)
    {
        return Duration.nanoseconds(nanoseconds);
    }

    @Override
    public final T newTime(Nanoseconds nanoseconds)
    {
        return onNewTime(inRange(nanoseconds));
    }

    /**
     * @return This time plus one unit
     */
    public T next()
    {
        return plusUnits(1);
    }

    public abstract T onNewTime(Nanoseconds nanoseconds);

    /**
     * @return This time plus the given number of units
     */
    public T plusUnits(double units)
    {
        return newTime(nanoseconds().plus(unitsToNanoseconds(units)));
    }

    @Override
    public String toString()
    {
        return String.valueOf(asUnits());
    }

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
