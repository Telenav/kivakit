package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
public class Second extends BaseTime<Second>
{
    static final long nanosecondsPerSecond = (long) 1E9;

    public static Second second(long second)
    {
        ensure(Longs.isBetweenInclusive(second, 0, 59));
        return new Second(second);
    }

    protected Second(long second)
    {
        super(second * nanosecondsPerSecond);
    }

    @Override
    public Second maximum()
    {
        return second(59);
    }

    @Override
    public Second minimum()
    {
        return second(0);
    }

    @Override
    public long nanosecondsPerUnit()
    {
        return nanosecondsPerSecond;
    }

    @Override
    public Second newTime(long nanoseconds)
    {
        return new Second(nanoseconds);
    }
}
