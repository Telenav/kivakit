package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
public class Second extends BaseTime<Second>
{
    public static Second second(long second)
    {
        ensure(Longs.isBetweenInclusive(second, 0, 59));
        return new Second(second);
    }

    protected Second(long second)
    {
        super(second * 1_000L);
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
    public Second newInstance(long minute)
    {
        return second(minute);
    }
}


