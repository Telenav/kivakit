package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.value.count.BaseCount;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
public class Minute extends BaseCount<Minute>
{
    public static Minute minute(int minuteOfHour)
    {
        ensure(Ints.isBetweenInclusive(minuteOfHour, 0, 59));
        return new Minute(minuteOfHour);
    }

    protected Minute(int minuteOfHour)
    {
        super(minuteOfHour);
    }

    @Override
    public Minute newInstance(long minuteOfHour)
    {
        return minute((int) minuteOfHour);
    }
}
