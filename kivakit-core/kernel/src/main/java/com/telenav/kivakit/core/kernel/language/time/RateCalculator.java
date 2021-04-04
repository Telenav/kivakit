////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.time;

public class RateCalculator
{
    private double count;

    private double lastCount;

    private Time start;

    private Time lastStart;

    private final Duration resetInterval;

    public RateCalculator(final Duration resetInterval)
    {
        this.resetInterval = resetInterval;
        reset();
    }

    public void add(final int count)
    {
        this.count += count;
    }

    public void increment()
    {
        count++;
    }

    public Rate rate()
    {
        final var elapsed = start.elapsedSince();
        final Rate rate;
        if (lastStart != null)
        {
            rate = new Rate(lastCount, resetInterval);
        }
        else
        {
            rate = new Rate(count, elapsed);
        }
        if (elapsed.isGreaterThan(resetInterval))
        {
            reset();
        }
        return rate;
    }

    private void reset()
    {
        lastCount = count;
        lastStart = start;
        count = 0;
        start = Time.now();
    }
}
