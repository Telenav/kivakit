package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.time.DayOfWeek.Standard;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.Day.Type.DAY;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_MONTH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_UNIX_EPOCH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_WEEK;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_YEAR;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.ISO;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.JAVA;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static com.telenav.kivakit.core.time.Hour.nanosecondsPerHour;
import static java.lang.Integer.MAX_VALUE;

/**
 * Represents a day of some {@link Type}, such as a day of the week, month or year.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@Tested
public class Day extends BaseTime<Day>
{
    static final long nanosecondsPerDay = nanosecondsPerHour * 24;

    /**
     * @return An absolute day from 0 to n
     */
    @Tested
    public static Day day(int day)
    {
        return new Day(DAY, null, day);
    }

    /**
     * @return A day since the start of a month, from 1 to 31
     */
    @Tested
    public static Day dayOfMonth(int day)
    {
        return new Day(DAY_OF_MONTH, null, day);
    }

    /**
     * @return A day since the start of the UNIX epoch, from 0 to n
     */
    @Tested
    public static Day dayOfUnixEpoch(int day)
    {
        return new Day(DAY_OF_UNIX_EPOCH, null, day);
    }

    /**
     * @return A day since the start of the week in the given day-of-week {@link Standard}
     */
    @Tested
    public static Day dayOfWeek(int day, Standard standard)
    {
        return new Day(DAY_OF_WEEK, standard, day);
    }

    /**
     * @return A day since the start of the year, from 0 to 365 (in leap years)
     */
    @Tested
    public static Day dayOfYear(int day)
    {
        return new Day(DAY_OF_YEAR, null, day);
    }

    /**
     * The type of day
     */
    @NoTestRequired
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

    /** The standard for numbering days of the week */
    private final Standard standard;

    /** The type of day this is */
    private final Type type;

    @NoTestRequired
    protected Day(Type type, Standard standard, int day)
    {
        super(day);

        this.type = type;
        this.standard = standard;

        ensure(isValid());
    }

    /**
     * @return This day as a day of the week if it is a day of the week, otherwise, an exception will be thrown.
     */
    @Tested
    public DayOfWeek asDayOfWeek()
    {
        ensure(type() == DAY_OF_WEEK);

        return standard == JAVA
                ? javaDayOfWeek(asUnits())
                : isoDayOfWeek(asUnits());
    }

    /**
     * @return This day as a zero-based index
     */
    @Tested
    public int asIndex()
    {
        switch (type())
        {
            case DAY:
            case DAY_OF_UNIX_EPOCH:
            case DAY_OF_YEAR:
                return asUnits();

            case DAY_OF_WEEK:
                return standard == JAVA
                        ? asUnits() - 1
                        : asUnits();

            case DAY_OF_MONTH:
                return asUnits() - 1;

            default:
                return unsupported();
        }
    }

    /**
     * @return False if this day is invalid, true if it might be valid. If true is returned, the day might still be
     * invalid in some context, for example, the 31st day of the month doesn't exist for all months.
     */
    @Tested
    public boolean isValid()
    {
        switch (type)
        {
            case DAY:
                return asUnits() >= 0;

            case DAY_OF_MONTH:
                return Ints.isBetweenInclusive(asUnits(), 1, 31);

            case DAY_OF_WEEK:
                if (standard() == ISO)
                {
                    return Ints.isBetweenInclusive(asUnits(), 0, 6);
                }
                if (standard() == JAVA)
                {
                    return Ints.isBetweenInclusive(asUnits(), 1, 7);
                }
                return unsupported();

            case DAY_OF_YEAR:
                return Ints.isBetweenInclusive(asUnits(), 0, 365);

            case DAY_OF_UNIX_EPOCH:
                return Ints.isBetweenInclusive(asUnits(), 0, MAX_VALUE);

            default:
                return unsupported();
        }
    }

    @Override
    public Day maximum()
    {
        switch (type)
        {
            case DAY_OF_WEEK:
                return dayOfWeek(6, ISO);

            case DAY_OF_UNIX_EPOCH:
                return dayOfUnixEpoch(nanosecondsToUnits(MAX_VALUE));

            case DAY:
                return day(nanosecondsToUnits(MAX_VALUE));

            case DAY_OF_MONTH:
            case DAY_OF_YEAR:
            default:
                return unsupported();
        }
    }

    @Override
    public Day minimum()
    {
        return day(0);
    }

    @Override
    public long nanosecondsPerUnit()
    {
        return nanosecondsPerDay;
    }

    @Override
    public Day newTime(long nanoseconds)
    {
        return new Day(type, standard, nanosecondsToUnits(nanoseconds));
    }

    @NoTestRequired
    public Standard standard()
    {
        return standard;
    }

    @Tested
    public Type type()
    {
        return type;
    }
}
