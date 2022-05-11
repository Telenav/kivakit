package com.telenav.kivakit.core.time;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;

@SuppressWarnings("unused")
public class Year extends BaseTime<Year>
{
    static final long nanosecondsPerYear = (long) (365.25 * Day.nanosecondsPerDay);

    public static Year year(int year)
    {
        ensure(year >= 1970 && year < 2038, "Invalid year: $", year);
        return new Year(year);
    }

    protected Year(int year)
    {
        super(year * nanosecondsPerYear);
    }

    public Time at(Month month)
    {
        return LocalTime.localTime(utcTimeZone(), this, month, Day.dayOfMonth(1), Hour.militaryHour(0), Minute.minute(0), Second.second(0));
    }

    @Override
    public Year maximum()
    {
        return year(2038);
    }

    @Override
    public Year minimum()
    {
        return year(1970);
    }

    @Override
    public long nanosecondsPerUnit()
    {
        return nanosecondsPerYear;
    }

    @Override
    public Year newTime(long nanoseconds)
    {
        return year(nanosecondsToUnits(nanoseconds));
    }
}
