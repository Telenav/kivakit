package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.Second.nanosecondsPerSecond;

@SuppressWarnings("unused")
public class Minute extends BaseTime<Minute>
{
    static final long nanosecondsPerMinute = nanosecondsPerSecond * 60;

    public static Minute minute(long minute)
    {
        ensure(Longs.isBetweenInclusive(minute, 0, 59));
        return new Minute(minute);
    }

    protected Minute(long minute)
    {
        super(minute * 60 * 1_000);
    }

    @Override
    public Minute maximum()
    {
        return minute(59);
    }

    @Override
    public Minute minimum()
    {
        return minute(0);
    }

    @Override
    public long nanosecondsPerUnit()
    {
        return nanosecondsPerMinute;
    }

    @Override
    public Minute newTime(long nanoseconds)
    {
        return new Minute(nanoseconds);
    }
}
