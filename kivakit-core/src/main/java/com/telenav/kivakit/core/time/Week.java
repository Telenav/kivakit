package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
public class Week extends BaseTime<Week>
{
    private static final long millisecondsPer = 7 * 24 * 60 * 60 * 1_000;

    public static Week weeks(long weeks)
    {
        ensure(Longs.isBetweenInclusive(weeks, 0, 59));
        return new Week(weeks);
    }

    protected Week(long weeks)
    {
        super(weeks * millisecondsPer);
    }

    @Override
    public Week maximum()
    {
        return weeks(Long.MAX_VALUE);
    }

    @Override
    public Week minimum()
    {
        return weeks(0);
    }

    @Override
    public Week newInstance(long milliseconds)
    {
        return weeks(milliseconds / millisecondsPer);
    }
}
