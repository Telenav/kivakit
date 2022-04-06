package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.value.count.BaseCount;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
public class Year extends BaseCount<Year>
{
    public static Year year(int year)
    {
        ensure(year >= 1970 && year < 2100);
        return new Year(year);
    }

    protected Year(int year)
    {
        super(year);
    }

    @Override
    public Year newInstance(long year)
    {
        return year((int) year);
    }
}
