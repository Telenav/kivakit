package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.time.Nanoseconds;

import static com.telenav.kivakit.core.time.BaseTime.Topology.LINEAR;
import static com.telenav.kivakit.core.time.Day.nanosecondsPerDay;

@SuppressWarnings("unused")
public class Week extends BaseTime<Week>
{
    private static final Nanoseconds nanosecondsPerWeek = nanosecondsPerDay.times(7);

    public static Week week(int week)
    {
        return new Week(week);
    }

    protected Week(int week)
    {
        super(nanosecondsPerWeek.times(week));
    }

    @Override
    public Week maximum()
    {
        return week((int) 1E9);
    }

    @Override
    public Week minimum()
    {
        return week(0);
    }

    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerWeek;
    }

    @Override
    public Week onNewTime(Nanoseconds nanoseconds)
    {
        return week((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    protected Topology topology()
    {
        return LINEAR;
    }
}
