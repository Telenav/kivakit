////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.locks.legacy;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;

public class NotifyAllBooleanLock implements BooleanLock
{
    protected volatile boolean state;

    public NotifyAllBooleanLock()
    {
        this(false);
    }

    public NotifyAllBooleanLock(final boolean state)
    {
        this.state = state;
    }

    @Override
    public synchronized boolean get()
    {
        return state;
    }

    @Override
    public synchronized void set(final boolean state)
    {
        if (state != this.state)
        {
            this.state = state;
            notifyAll();
        }
    }

    @Override
    public synchronized boolean waitFor(final boolean state, final Duration maximumWaitTime)
    {
        final var startedWaiting = Time.now();
        while (state != this.state)
        {
            final var beenWaiting = startedWaiting.elapsedSince();
            if (beenWaiting.isGreaterThanOrEqualTo(maximumWaitTime))
            {
                return false;
            }
            else
            {
                maximumWaitTime.subtract(beenWaiting).wait(this);
            }
        }
        return true;
    }
}
