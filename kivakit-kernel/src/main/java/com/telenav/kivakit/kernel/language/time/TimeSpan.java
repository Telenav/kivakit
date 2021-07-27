package com.telenav.kivakit.kernel.language.time;

public class TimeSpan
{
    public static TimeSpan all()
    {
        return new TimeSpan(Time.START_OF_UNIX_TIME, Time.MAXIMUM);
    }

    public static TimeSpan of(final Time start, final Time end)
    {
        return new TimeSpan(start, end);
    }

    public static TimeSpan of(final Time start, final Duration duration)
    {
        return new TimeSpan(start, start.plus(duration));
    }

    private final Time start;

    private final Time end;

    protected TimeSpan(final Time start, final Time end)
    {
        this.start = start;
        this.end = end;
    }

    /**
     * @return True if this time span contains the given time (endpoints are inclusive)
     */
    public boolean contains(final Time time)
    {
        return time.isAtOrAfter(start) && time.isAtOrBefore(end);
    }

    public Duration duration()
    {
        return end.minus(start);
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
