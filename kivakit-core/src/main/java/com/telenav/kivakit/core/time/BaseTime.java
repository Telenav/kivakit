package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.interfaces.time.PointInTime;

import java.util.Objects;

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
 *     <li>{@link #nanosecondsToUnits(long)}</li>
 *     <li>{@link #unitsToNanoseconds(int)}</li>
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
 *     <li>{@link #minus(int)}</li>
 *     <li>{@link #plus(LengthOfTime)}</li>
 *     <li>{@link #plus(int)}</li>
 *     <li>{@link #next()}</li>
 *     <li>{@link #nearest(LengthOfTime)}</li>
 *     <li>{@link #roundUp(LengthOfTime)}</li>
 *     <li>{@link #roundDown(LengthOfTime)}</li>
 * </ul>
 *
 * <p><b>Implementation</b></p>
 *
 * <ul>
 *     <li>{@link #newDuration(long)}</li>
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
public abstract class BaseTime<T extends BaseTime<T>> implements PointInTime<T, Duration>
{
    /** The number of nanoseconds since start of UNIX time */
    private long nanoseconds;

    public BaseTime()
    {
    }

    public BaseTime(long nanoseconds)
    {
        this.nanoseconds = nanoseconds;
    }

    /**
     * @return This point in time in units
     */
    public int asUnits()
    {
        return nanosecondsToUnits(nanoseconds());
    }

    /**
     * @return This time minus one unit
     */
    public T decremented()
    {
        return minus(1);
    }

    @Override
    @Tested
    public boolean equals(final Object object)
    {
        if (object instanceof Hour)
        {
            var that = (Hour) object;
            return this.quantum() == that.quantum();
        }
        return false;
    }

    @Override
    @Tested
    public int hashCode()
    {
        return Objects.hash(quantum());
    }

    /**
     * @return This time plus one unit
     */
    public T incremented()
    {
        return plus(1);
    }

    public boolean isBetweenExclusive(T minimum, T maximum)
    {
        return asUnits() >= minimum.asUnits() && asUnits() < maximum.asUnits();
    }

    public boolean isBetweenInclusive(T minimum, T maximum)
    {
        return asUnits() >= minimum.asUnits() && asUnits() <= maximum.asUnits();
    }

    /**
     * @return This time minus the given number of units
     */
    public T minus(int units)
    {
        return newTime(nanoseconds() - unitsToNanoseconds(units));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nanoseconds()
    {
        return nanoseconds;
    }

    /**
     * Returns the number of milliseconds per unit of time
     */
    public abstract long nanosecondsPerUnit();

    @Override
    public Duration newDuration(long nanoseconds)
    {
        return Duration.nanoseconds(nanoseconds);
    }

    /**
     * @return This time plus one unit
     */
    public T next()
    {
        return plus(1);
    }

    /**
     * @return This time plus the given number of units
     */
    public T plus(int units)
    {
        return newTime(nanoseconds() + unitsToNanoseconds(units));
    }

    @Override
    public String toString()
    {
        return Integer.toString(asUnits());
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Converts the given number of nanoseconds to units
     */
    protected int nanosecondsToUnits(long nanoseconds)
    {
        return (int) (nanoseconds / nanosecondsPerUnit());
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Converts the given number of units to nanoseconds
     */
    protected long unitsToNanoseconds(int units)
    {
        return units * nanosecondsPerUnit();
    }
}
