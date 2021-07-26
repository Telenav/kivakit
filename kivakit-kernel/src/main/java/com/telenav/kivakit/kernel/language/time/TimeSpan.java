package com.telenav.kivakit.kernel.language.time;

public class TimeSpan
{
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

    public TimeSpan(final Time start, final Time end)
    {
        this.start = start;
        this.end = end;
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
