package com.telenav.kivakit.core.time;

@SuppressWarnings("unused")
public class TimeSpan
{
    public static TimeSpan all()
    {
        return new TimeSpan(Time.START_OF_UNIX_TIME, Time.MAXIMUM);
    }

    public static TimeSpan future(Duration duration)
    {
        var now = Time.now();
        return of(now, now.plus(duration));
    }

    public static TimeSpan of(Time start, Duration duration)
    {
        return new TimeSpan(start, start.plus(duration));
    }

    public static TimeSpan of(Time start, Time end)
    {
        return new TimeSpan(start, end);
    }

    public static TimeSpan past(Duration duration)
    {
        var now = Time.now();
        return of(now.minus(duration), now);
    }

    private final Time end;

    private final Time start;

    protected TimeSpan(Time start, Time end)
    {
        this.start = start;
        this.end = end;
    }

    /**
     * @return True if this time span contains the given time (endpoints are inclusive)
     */
    public boolean contains(Time time)
    {
        return time.isAtOrAfter(start) && time.isAtOrBefore(end);
    }

    public Duration duration()
    {
        return end.elapsedSince(start);
    }

    public Time end()
    {
        return end;
    }

    public Time start()
    {
        return start;
    }
}
