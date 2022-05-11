package com.telenav.kivakit.core.time;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;

@SuppressWarnings("unused")
public class Year extends BaseTime<Year>
{
    public static Year year(int year)
    {
        ensure(year >= 1970 && year < 3000);
        return new Year(year);
    }

    protected Year(int year)
    {
        super(year);
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
        return unsupported();
    }

    @Override
    public Year newTime(long nanoseconds)
    {
        return year(nanosecondsToUnits(nanoseconds));
    }
}
