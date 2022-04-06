package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.value.count.BaseCount;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
public class Second extends BaseCount<Second>
{
    public static Second second(int second)
    {
        ensure(Ints.isBetweenInclusive(second, 0, 59));
        return new Second(second);
    }

    protected Second(int second)
    {
        super(second);
    }

    @Override
    public Second newInstance(long second)
    {
        return second((int) second);
    }
}
