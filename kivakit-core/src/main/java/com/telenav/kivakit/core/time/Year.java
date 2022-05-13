package com.telenav.kivakit.core.time;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;

@SuppressWarnings("unused")
public class Year extends BaseTime<Year>
{
    static final long nanosecondsPerYear = (long) (365.25 * Day.nanosecondsPerDay);

    public static final int UNIX_EPOCH_START_YEAR = 1970;

    public static Year year(int year)
    {
        ensure(year >= UNIX_EPOCH_START_YEAR && year < 2038, "Invalid year: $", year);

        return new Year((year - UNIX_EPOCH_START_YEAR) * nanosecondsPerYear);
    }

    protected Year(long nanoseconds)
    {
        super(nanoseconds);
    }

    @Override
    public int asUnits()
    {
        return UNIX_EPOCH_START_YEAR + super.asUnits();
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
        return year(UNIX_EPOCH_START_YEAR);
    }

    @Override
    public long nanosecondsPerUnit()
    {
        return nanosecondsPerYear;
    }

    @Override
    public Year newTime(long nanoseconds)
    {
        return new Year(nanoseconds);
    }
}
