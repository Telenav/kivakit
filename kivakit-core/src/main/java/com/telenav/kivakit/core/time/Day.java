package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.time.Nanoseconds;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.language.primitive.Ints.intIsBetweenInclusive;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;
import static com.telenav.kivakit.core.time.BaseTime.Topology.LINEAR;
import static com.telenav.kivakit.core.time.Day.Type.DAY;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_MONTH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_UNIX_EPOCH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_WEEK;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_YEAR;
import static com.telenav.kivakit.core.time.Hour.nanosecondsPerHour;
import static java.lang.Integer.MAX_VALUE;

/**
 * Represents a day of some {@link Type}, such as a day of the week, month or year.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Day extends BaseTime<Day>
{
    /** The number of nanoseconds per day */
    static final Nanoseconds nanosecondsPerDay = nanosecondsPerHour.times(24);

    /**
     * Returns an absolute day from 0 to n
     */
    public static Day day(int day)
    {
        return new Day(DAY, day);
    }

    /**
     * Returns a day since the start of a month, from 1 to 31
     */
    public static Day dayOfMonth(int day)
    {
        return new Day(DAY_OF_MONTH, day - 1);
    }

    /**
     * Returns a day since the start of the UNIX epoch, from 0 to n
     */
    public static Day dayOfUnixEpoch(int day)
    {
        return new Day(DAY_OF_UNIX_EPOCH, day);
    }

    /**
     * Returns a day since the start of the year, from 0 to 365 (in leap years)
     */
    public static Day dayOfYear(int day)
    {
        return new Day(DAY_OF_YEAR, day);
    }

    /**
     * Returns a day since the start of the week as an ISO ordinal
     */
    public static Day isoDayOfWeek(int day)
    {
        return new Day(DAY_OF_WEEK, day);
    }

    /**
     * Returns a day since the start of the week as a Java ordinal
     */
    public static Day javaDayOfWeek(int day)
    {
        return new Day(DAY_OF_WEEK, day - 1);
    }

    /**
     * The type of day
     */
    public enum Type
    {
        /** A number of days */
        DAY,

        /** A day of the UNIX epoch */
        DAY_OF_UNIX_EPOCH,

        /** A day of the month (from 1 to */
        DAY_OF_MONTH,

        /** A day of the week (stored as ISO, from 0 to 6, inclusive) */
        DAY_OF_WEEK,

        /** A day of the year */
        DAY_OF_YEAR
    }

    /** The type of day this is */
    private Type type;

    protected Day()
    {
    }

    /**
     * @param type The type of day
     * @param day A zero-based ordinal value
     */
    protected Day(Type type, int day)
    {
        super(nanosecondsPerDay.times(day));

        this.type = type;

        ensure(isValid());
    }

    /**
     * Returns this day as a day of the week if it is a day of the week, otherwise, an exception will be thrown.
     */
    public DayOfWeek asDayOfWeek()
    {
        ensure(type() == DAY_OF_WEEK);

        return DayOfWeek.isoDayOfWeek(asUnits());
    }

    /**
     * Returns this day as a zero-based index
     */
    public int asIndex()
    {
        return super.asUnits();
    }

    /**
     * The number of days (zero based), or the day of the month (one based)
     * <p>
     * {@inheritDoc}
     */
    @Override
    public int asUnits()
    {
        var units = super.asUnits();

        switch (type)
        {
            case DAY:
            case DAY_OF_WEEK:
            case DAY_OF_YEAR:
            case DAY_OF_UNIX_EPOCH:
                return units;

            case DAY_OF_MONTH:
                return units + 1;

            default:
                return unsupported();
        }
    }

    /**
     * Returns false if this day is invalid, true if it might be valid. If true is returned, the day might still be
     * invalid in some context, for example, the 31st day of the month doesn't exist for all months.
     */
    public boolean isValid()
    {
        switch (type)
        {
            case DAY:
                return asUnits() >= 0;

            case DAY_OF_MONTH:
                return intIsBetweenInclusive(asUnits(), 1, 31);

            case DAY_OF_WEEK:
                return intIsBetweenInclusive(asUnits(), 0, 6);

            case DAY_OF_YEAR:
                return intIsBetweenInclusive(asUnits(), 0, 365);

            case DAY_OF_UNIX_EPOCH:
                return intIsBetweenInclusive(asUnits(), 0, MAX_VALUE);

            default:
                return unsupported();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Day maximum()
    {
        switch (type)
        {
            case DAY_OF_WEEK:
                return isoDayOfWeek(6);

            case DAY_OF_UNIX_EPOCH:
            case DAY:
                return dayOfUnixEpoch((int) 1E9);

            case DAY_OF_MONTH:
            case DAY_OF_YEAR:
            default:
                return unsupported();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Day minimum()
    {
        switch (type)
        {
            case DAY_OF_MONTH:
            case DAY_OF_YEAR:
                return unsupported();

            default:
                return day(0);
        }
    }

    /**
     * Returns the number of nanoseconds per day
     */
    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerDay;
    }

    /**
     * Creates a day object for the given nanoseconds
     */
    @Override
    public Day onNewTime(Nanoseconds nanoseconds)
    {
        return new Day(type, (int) nanosecondsToUnits(nanoseconds));
    }

    /**
     * Returns the type of day this object is representing
     */
    public Type type()
    {
        return type;
    }

    /**
     * Returns the topology (linearity or circularity) of this time object
     */
    @Override
    protected Topology topology()
    {
        return type() == DAY || type() == DAY_OF_UNIX_EPOCH
                ? LINEAR
                : CYCLIC;
    }
}
