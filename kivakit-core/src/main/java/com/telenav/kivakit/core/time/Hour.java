package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;

import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.Hour.Type.HOUR;
import static com.telenav.kivakit.core.time.Hour.Type.HOUR_OF_MERIDIEM;
import static com.telenav.kivakit.core.time.Hour.Type.MILITARY_HOUR;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.time.Meridiem.AM;
import static com.telenav.kivakit.core.time.Meridiem.NO_MERIDIEM;
import static com.telenav.kivakit.core.time.Meridiem.PM;
import static com.telenav.kivakit.core.time.Meridiem.meridiemHour;

/**
 * Represents an hour of the day.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #am(int)} - A morning hour</li>
 *     <li>{@link #militaryHour(long)} - An hour of the day on a 24-hour clock</li>
 *     <li>{@link #hourOfDay(int, Meridiem)} - An hour of the day, AM or PM</li>
 *     <li>{@link #midnight()} - Hour zero</li>
 *     <li>{@link #noon()} - Hour twelve</li>
 *     <li>{@link #pm(int)} - An afternoon or evening hour</li>
 * </ul>
 *
 * <p><b>Accessors</b></p>
 *
 * <ul>
 *     <li>{@link #asInt()} - The hour</li>
 *     <li>{@link #meridiem()} - AM, PM or 24-hour</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asMilitaryHour()} - The hour on a 24-hour clock</li>
 *     <li>{@link #asMeridiemHour()} - The hour, either AM or PM</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class Hour extends BaseTime<Hour>
{
    private static final long millisecondsPer = 60 * 60 * 1_000;

    @Tested
    public static Hour am(int hour)
    {
        return hourOfDay(hour, AM);
    }

    @Tested
    public static Hour hour(int hour)
    {
        return new Hour(HOUR, NO_MERIDIEM, hour);
    }

    @Tested
    public static Hour hourOfDay(int hour, Meridiem meridiem)
    {
        return new Hour(HOUR_OF_MERIDIEM, meridiem, hour);
    }

    @Tested
    public static Hour midnight()
    {
        return am(12);
    }

    @Tested
    public static Hour militaryHour(long militaryHouor)
    {
        return new Hour(MILITARY_HOUR, NO_MERIDIEM, militaryHouor);
    }

    @Tested
    public static Hour noon()
    {
        return pm(12);
    }

    @Tested
    public static Hour pm(int hour)
    {
        return hourOfDay(hour, PM);
    }

    @NoTestRequired
    public enum Type
    {
        HOUR(0, Integer.MAX_VALUE),
        HOUR_OF_WEEK(0, 7 * 24),
        MILITARY_HOUR(0, 24),
        HOUR_OF_MERIDIEM(1, 12 + 1);

        private final int minimum;

        private final int maximumExclusive;

        @NoTestRequired
        Type(int minimum, int maximumExclusive)
        {
            this.minimum = minimum;
            this.maximumExclusive = maximumExclusive;
        }

        @Tested
        int ensureInRange(int hour)
        {
            ensure(Ints.isBetweenExclusive(hour, minimum, maximumExclusive), "Hour not valid: $", hour);
            return hour;
        }

        @Tested
        int militaryHour(Meridiem meridiem, int hour)
        {
            ensureInRange(hour);

            if (this == Type.HOUR_OF_MERIDIEM)
            {
                return meridiem.asMilitaryHour(hour);
            }
            return hour;
        }
    }

    /** The type of hour being modeled */
    private final Type type;

    @Tested
    protected Hour(Type type, Meridiem meridiem, long hour)
    {
        super(type.militaryHour(meridiem, (int) hour) * millisecondsPer);

        this.type = ensureNotNull(type);
    }

    @NoTestRequired
    public HourOfWeek asHourOfWeek()
    {
        return hourOfWeek(asMilitaryHour());
    }

    /**
     * Returns this hour of the day on a 12-hour AM/PM clock
     */
    @Tested
    public int asMeridiemHour()
    {
        return meridiemHour(asMilitaryHour());
    }

    /**
     * Returns this hour of the day on a 24-hour clock
     */
    @Tested
    public int asMilitaryHour()
    {
        return (int) (asLong() / millisecondsPer);
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

    @Override
    @Tested
    public Hour inRangeInclusive(long value)
    {
        var rounded = (value + 24) % 24;
        return super.inRangeInclusive(rounded + 1);
    }

    @Tested
    public boolean isMeridiem()
    {
        return type() == HOUR_OF_MERIDIEM;
    }

    @Tested
    public boolean isMilitary()
    {
        return type() == MILITARY_HOUR;
    }

    @Override
    public Hour maximum()
    {
        switch (type())
        {
            case HOUR:
                return newInstance(Long.MAX_VALUE);

            case MILITARY_HOUR:
                return militaryHour(23);

            case HOUR_OF_MERIDIEM:
                return hour(12);

            case HOUR_OF_WEEK:
                return hour(7 * 24 - 1);

            default:
                return unsupported();
        }
    }

    /**
     * Returns the {@link Meridiem} for this hour of the day
     */
    public Meridiem meridiem()
    {
        switch (type)
        {
            case HOUR_OF_MERIDIEM:
            case MILITARY_HOUR:
                return Meridiem.meridiem(asMilitaryHour());

            case HOUR:
            case HOUR_OF_WEEK:
                return NO_MERIDIEM;

            default:
                return unsupported();
        }
    }

    @Override
    public Hour minimum()
    {
        return militaryHour(0);
    }

    @Override
    public Hour newInstance(long count)
    {
        return militaryHour((int) (count / millisecondsPer));
    }

    @Override
    public Hour next()
    {
        switch (type())
        {
            case HOUR:
                return super.next();

            case HOUR_OF_MERIDIEM:
                return asMilitaryHour() == 11 ? null : super.next();

            case MILITARY_HOUR:
                return asMilitaryHour() == 23 ? null : super.next();

            case HOUR_OF_WEEK:
                return asMilitaryHour() == (7 * 24) - 1 ? null : super.next();

            default:
                return unsupported();
        }
    }

    @Override
    public long quantum()
    {
        return asMilitaryHour();
    }

    @Override
    @Tested
    public String toString()
    {
        switch (type())
        {
            case HOUR:
            case HOUR_OF_WEEK:
                return asSimpleString();

            case HOUR_OF_MERIDIEM:
            case MILITARY_HOUR:
            {
                return asMeridiemHour() + meridiem().name().toLowerCase();
            }

            default:
                return unsupported();
        }
    }

    public Type type()
    {
        return type;
    }
}
