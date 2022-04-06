package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.value.count.BaseCount;

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
 *     <li>{@link #militaryHour(int)} - An hour of the day on a 24-hour clock</li>
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
public class Hour extends BaseCount<Hour>
{
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
    public static Hour militaryHour(int militaryHouor)
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

    public enum Type
    {
        HOUR(0, Integer.MAX_VALUE),
        HOUR_OF_WEEK(0, 7 * 24),
        MILITARY_HOUR(0, 24),
        HOUR_OF_MERIDIEM(1, 12 + 1);

        private final int minimum;

        private final int maximumExclusive;

        Type(int minimum, int maximumExclusive)
        {
            this.minimum = minimum;
            this.maximumExclusive = maximumExclusive;
        }

        int ensureInRange(int hour)
        {
            ensure(Ints.isBetweenExclusive(hour, minimum, maximumExclusive));
            return hour;
        }

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

    private final Type type;

    protected Hour(Type type, Meridiem meridiem, long hour)
    {
        super(type.militaryHour(meridiem, (int) hour));

        this.type = ensureNotNull(type);
    }

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
        return asInt();
    }

    @Override
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
    public int hashCode()
    {
        return Objects.hash(quantum());
    }

    @Override
    @Tested
    public Hour inRange(long value)
    {
        return super.inRange((value + 24) % 24);
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
    @Tested
    public Hour maximum()
    {
        return militaryHour(23);
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
    @Tested
    public Hour minimum()
    {
        return militaryHour(0);
    }

    @Override
    public Hour newInstance(long count)
    {
        return militaryHour((int) count);
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
