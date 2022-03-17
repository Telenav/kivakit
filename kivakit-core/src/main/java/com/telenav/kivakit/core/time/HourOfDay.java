package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.value.count.AbstractCount;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenInclusive;

public class HourOfDay extends AbstractCount<HourOfDay>
{
    public static HourOfDay hourOfDay(int hourOfDay)
    {
        return new HourOfDay(hourOfDay);
    }

    protected HourOfDay(long count)
    {
        super(ensureBetweenInclusive(count, 0, 23));
    }

    @Override
    public HourOfDay maximum()
    {
        return hourOfDay(23);
    }

    @Override
    public HourOfDay minimum()
    {
        return hourOfDay(0);
    }

    @Override
    protected HourOfDay newInstance(long count)
    {
        return hourOfDay((int) count);
    }
}
