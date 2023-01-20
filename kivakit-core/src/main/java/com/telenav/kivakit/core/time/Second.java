package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.time.Nanoseconds;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.language.primitive.Doubles.doubleIsBetweenInclusive;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;

/**
 * Represents a second on a clock
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Second extends BaseTime<Second>
{
    static final Nanoseconds nanosecondsPerSecond = Nanoseconds.nanoseconds(1E9);

    public static Second second(int second)
    {
        ensure(doubleIsBetweenInclusive(second, 0, 59));

        return new Second(second);
    }

    protected Second()
    {
    }

    protected Second(int second)
    {
        super(nanosecondsPerSecond.times(second));
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
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerSecond;
    }

    @Override
    public Second onNewTime(Nanoseconds nanoseconds)
    {
        return new Second((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    protected Topology topology()
    {
        return CYCLIC;
    }
}
