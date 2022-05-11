package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.Day.nanosecondsPerDay;
import static java.lang.Long.MAX_VALUE;

@SuppressWarnings("unused")
public class Week extends BaseTime<Week>
{
    private static final long nanosecondsPerWeek = nanosecondsPerDay * 7;

    public static Week weeks(int weeks)
    {
        ensure(Longs.isBetweenInclusive(weeks, 0, 59));
        return new Week(weeks);
    }

    protected Week(int weeks)
    {
        super(weeks * nanosecondsPerWeek);
    }

    @Override
    public Week maximum()
    {
        return weeks(nanosecondsToUnits(MAX_VALUE));
    }

    @Override
    public Week minimum()
    {
        return weeks(0);
    }

    @Override
    public long nanosecondsPerUnit()
    {
        return nanosecondsPerWeek;
    }

    @Override
    public Week newTime(long nanoseconds)
    {
        return weeks(nanosecondsToUnits(nanoseconds));
    }
}
