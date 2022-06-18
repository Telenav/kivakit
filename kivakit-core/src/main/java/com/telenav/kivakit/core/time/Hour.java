package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.testing.NoTestRequired;
import com.telenav.kivakit.core.testing.Tested;
import com.telenav.kivakit.interfaces.time.Nanoseconds;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.time.Meridiem.AM;
import static com.telenav.kivakit.core.time.Meridiem.PM;
import static com.telenav.kivakit.core.time.Meridiem.meridiemHour;
import static com.telenav.kivakit.core.time.Minute.nanosecondsPerMinute;

/**
 * Represents an hour on a clock, either as a military hour or using the meridiem (am/pm) system.
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
 *     <li>{@link #asUnits()} - The hour</li>
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
    static final Nanoseconds nanosecondsPerHour = nanosecondsPerMinute.times(60);

    @Tested
    public static Hour am(int hour)
    {
        return hourOfDay(hour, AM);
    }

    @Tested
    public static Hour hourOfDay(int hour, Meridiem meridiem)
    {
        return new Hour(meridiem.asMilitaryHour(hour));
    }

    @Tested
    public static Hour midnight()
    {
        return am(12);
    }

    @Tested
    public static Hour militaryHour(int militaryHour)
    {
        ensure(militaryHour >= 0);
        ensure(militaryHour <= 23);

        return new Hour(militaryHour);
    }

    public static List<Hour> militaryHours()
    {
        var hours = new ArrayList<Hour>();
        for (int i = 0; i < 24; i++)
        {
            hours.add(militaryHour(i));
        }
        return hours;
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

    protected Hour()
    {
    }

    @Tested
    protected Hour(int militaryHour)
    {
        super(nanosecondsPerHour.times(militaryHour));
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
        return (int) nanoseconds().dividedBy(nanosecondsPerHour);
    }

    @Override
    public Hour maximum()
    {
        return militaryHour(23);
    }

    /**
     * Returns the {@link Meridiem} for this hour of the day
     */
    public Meridiem meridiem()
    {
        return asMilitaryHour() <= 11 ? AM : PM;
    }

    @Override
    public Hour minimum()
    {
        return militaryHour(0);
    }

    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerHour;
    }

    @Override
    public Hour onNewTime(Nanoseconds nanoseconds)
    {
        return militaryHour((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    @Tested
    public String toString()
    {
        return asMeridiemHour() + meridiem().name().toLowerCase();
    }

    @Override
    protected Topology topology()
    {
        return CYCLIC;
    }
}
