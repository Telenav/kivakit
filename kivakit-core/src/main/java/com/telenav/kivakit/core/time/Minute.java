package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.time.Nanoseconds;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.language.primitive.Ints.intIsBetweenInclusive;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;
import static com.telenav.kivakit.core.time.Second.nanosecondsPerSecond;

/**
 * Represents a minute on a clock
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Minute extends BaseTime<Minute>
{
    static final Nanoseconds nanosecondsPerMinute = nanosecondsPerSecond.times(60);

    public static Minute minute(int minute)
    {
        ensure(intIsBetweenInclusive(minute, 0, 59));

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
