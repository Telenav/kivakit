package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A span of time
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #timeSpan(Time, Duration)}</li>
 *     <li>{@link #timeSpan(Time, Time)}</li>
 *     <li>{@link #timeSpanFromNow(Duration)}</li>
 *     <li>{@link #timeSpanToNow(Duration)}</li>
 *     <li>{@link #unixEpoch()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #end()}</li>
 *     <li>{@link #length()}</li>
 *     <li>{@link #start()}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #contains(Time)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class TimeSpan
{
    /**
     * Returns a timespan from the given start time to the given start time plus the given duration
     */
    public static TimeSpan timeSpan(Time start, Duration duration)
    {
        return new TimeSpan(start, start.plus(duration));
    }

    /**
     * Returns a timespan from the given start time to the given end time
     */
    public static TimeSpan timeSpan(Time start, Time end)
    {
        return new TimeSpan(start, end);
    }

    /**
     * Returns a timespan from now to the the given duration in the future
     */
    public static TimeSpan timeSpanFromNow(Duration duration)
    {
        var now = Time.now();
        return timeSpan(now, now.plus(duration));
    }

    /**
     * Returns a timespan from the given duration in the past to now
     */
    public static TimeSpan timeSpanToNow(Duration duration)
    {
        var now = Time.now();
        return timeSpan(now.minus(duration), now);
    }

    /**
     * The entire UNIX timespan
     */
    public static TimeSpan unixEpoch()
    {
        return new TimeSpan(Time.START_OF_UNIX_TIME, Time.MAXIMUM);
    }

    /** The start of this timespan */
    private final Time end;

    /** The end of this timespan */
    private final Time start;

    protected TimeSpan(Time start, Time end)
    {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns true if this time span contains the given time (endpoints are inclusive)
     */
    public boolean contains(Time time)
    {
        return time.isAtOrAfter(start) && time.isAtOrBefore(end);
    }

    /**
     * Returns the end of this timespan
     */
    public Time end()
    {
        return end;
    }

    /**
     * Returns the length of this timespan
     */
    public Duration length()
    {
        return end.elapsedSince(start);
    }

    /**
     * Returns the start of this timespan
     */
    public Time start()
    {
        return start;
    }
}
