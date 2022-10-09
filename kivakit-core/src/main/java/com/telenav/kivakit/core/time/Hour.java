package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.time.Nanoseconds;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.time.Meridiem.AM;
import static com.telenav.kivakit.core.time.Meridiem.PM;
import static com.telenav.kivakit.core.time.Minute.nanosecondsPerMinute;

/**
 * Represents an hour on a clock, either as a military hour or using the meridiem (am/pm) system.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #am(int)} - A morning hour</li>
 *     <li>{@link #militaryHour(int)} - An hour of the day on a 24-hour clock</li>
 *     <li>{@link #militaryHours()} - All 24 military hours</li>
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
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Hour extends BaseTime<Hour>
{
    /** The number of nanoseconds in an hour */
    static final Nanoseconds nanosecondsPerHour = nanosecondsPerMinute.times(60);

    /**
     * Gets the given hour, anti-meridiem
     */
    public static Hour am(int hour)
    {
        return hourOfDay(hour, AM);
    }

    /**
     * Gets the hour of the day using the given meridiem
     *
     * @param hour The hour
     * @param meridiem AM or PM
     * @return The hour
     */
    public static Hour hourOfDay(int hour, Meridiem meridiem)
    {
        return new Hour(meridiem.asMilitaryHour(hour));
    }

    /**
     * Returns the midnight hour
     */
    public static Hour midnight()
    {
        return am(12);
    }

    /**
     * Returns the given military hour
     */
    public static Hour militaryHour(int militaryHour)
    {
        ensure(militaryHour >= 0);
        ensure(militaryHour <= 23);

        return new Hour(militaryHour);
    }

    /**
     * Returns the 24 military hours
     */
    public static List<Hour> militaryHours()
    {
        var hours = new ArrayList<Hour>();
        for (int i = 0; i < 24; i++)
        {
            hours.add(militaryHour(i));
        }
        return hours;
    }

    /**
     * Returns the noon hour
     */
    public static Hour noon()
    {
        return pm(12);
    }

    /**
     * Returns the given hour, post-meridiem
     */
    public static Hour pm(int hour)
    {
        return hourOfDay(hour, PM);
    }

    protected Hour()
    {
    }

    protected Hour(int militaryHour)
    {
        super(nanosecondsPerHour.times(militaryHour));
    }

    /**
     * Returns this hour as an hour of the week
     */
    public HourOfWeek asHourOfWeek()
    {
        return hourOfWeek(asMilitaryHour());
    }

    /**
     * Returns this hour of the day on a 12-hour AM/PM clock
     */
    public int asMeridiemHour()
    {
        return Meridiem.asMeridiemHour(asMilitaryHour());
    }

    /**
     * Returns this hour of the day on a 24-hour clock
     */
    public int asMilitaryHour()
    {
        return (int) nanoseconds().dividedBy(nanosecondsPerHour);
    }

    /**
     * Returns the maximum military hour
     */
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

    /**
     * Returns the minimum military hou
     */
    @Override
    public Hour minimum()
    {
        return militaryHour(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerHour;
    }

    /**
     * Returns a new Hour for the given nanosecondsv
     */
    @Override
    public Hour onNewTime(Nanoseconds nanoseconds)
    {
        return militaryHour((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    public String toString()
    {
        return asMeridiemHour() + meridiem().name().toLowerCase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Topology topology()
    {
        return CYCLIC;
    }
}
