package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.value.count.BaseCount;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenInclusive;

public class HourOfDay extends BaseCount<HourOfDay>
{
    @Tested
    public static HourOfDay am(int hour)
    {
        return hourOfDay(hour);
    }

    @Tested
    public static HourOfDay hourOfDay(int hourOfDay)
    {
        return new HourOfDay(hourOfDay);
    }

    @Tested
    public static HourOfDay midnight()
    {
        return am(0);
    }

    @Tested
    public static HourOfDay noon()
    {
        return pm(0);
    }

    @Tested
    public static HourOfDay pm(int hour)
    {
        return hourOfDay(hour + 12);
    }

    @Tested
    protected HourOfDay(long count)
    {
        super(ensureBetweenInclusive(count, 0, 23));
    }

    @Override
    @Tested
    public HourOfDay inRange(long value)
    {
        return super.inRange((value + 24) % 24);
    }

    @Override
    @Tested
    public HourOfDay maximum()
    {
        return hourOfDay(23);
    }

    @Override
    @Tested
    public HourOfDay minimum()
    {
        return hourOfDay(0);
    }

    @Override
    public String toString()
    {
        int hour = asInt();
        if (hour == 0)
        {
            return "midnight";
        }
        if (hour == 12)
        {
            return "noon";
        }
        return hour < 12
                ? hour + "am"
                : (hour - 12) + "pm";
    }

    @Override
    public HourOfDay newInstance(long count)
    {
        return hourOfDay((int) count);
    }
    @Override
    public HourOfDay newInstance(Long count)
    {
        return newInstance(count.longValue());
    }
}
