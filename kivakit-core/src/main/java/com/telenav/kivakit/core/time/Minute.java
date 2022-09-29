package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.interfaces.time.Nanoseconds;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;
import static com.telenav.kivakit.core.time.Second.nanosecondsPerSecond;

/**
 * Represents a minute on a clock
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Minute extends BaseTime<Minute>
{
    static final Nanoseconds nanosecondsPerMinute = nanosecondsPerSecond.times(60);

    public static Minute minute(int minute)
    {
        ensure(Ints.isBetweenInclusive(minute, 0, 59));

        return new Minute(minute);
    }

    protected Minute()
    {
    }

    protected Minute(int minute)
    {
        super(nanosecondsPerMinute.times(minute));
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
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerMinute;
    }

    @Override
    public Minute onNewTime(Nanoseconds nanoseconds)
    {
        return new Minute((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    protected Topology topology()
    {
        return CYCLIC;
    }
}
