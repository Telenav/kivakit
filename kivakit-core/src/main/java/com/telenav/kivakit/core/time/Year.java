package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.time.Nanoseconds;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.BaseTime.Topology.LINEAR;
import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;
import static com.telenav.kivakit.core.time.Minute.minute;
import static com.telenav.kivakit.core.time.Second.second;

@SuppressWarnings("unused")
public class Year extends BaseTime<Year>
{
    static final Nanoseconds nanosecondsPerYear = Day.nanosecondsPerDay.times(365.25);

    /**
     * Factory method
     *
     * @param year The UNIX epoch year, from 1970 to 2038
     * @return The year
     */
    public static Year unixEpochYear(int year)
    {
        ensure(year >= 1970 && year <= 2038);

        return year(year);
    }

    public static Year year(int year)
    {
        ensure(year >= 0);

        return new Year(year);
    }

    protected Year()
    {
    }

    protected Year(int year)
    {
        super(nanosecondsPerYear.times(year));
    }

    public int asEpochYear()
    {
        if (asYears() >= 1970 && asYears() < 2038)
        {
            return (int) asYears();
        }
        throw new IllegalStateException("Not an epoch year");
    }

    public Time at(Month month)
    {
        return LocalTime.localTime(utcTimeZone(),
                this,
                month,
                dayOfMonth(1),
                militaryHour(0),
                minute(0),
                second(0));
    }

    @Override
    public Year maximum()
    {
        return year((int) 1E9);
    }

    @Override
    public Year minimum()
    {
        return year(0);
    }

    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerYear;
    }

    @Override
    public Year onNewTime(Nanoseconds nanoseconds)
    {
        return new Year((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    protected Topology topology()
    {
        return LINEAR;
    }
}
