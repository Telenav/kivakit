////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.locks.legacy;

import com.telenav.kivakit.core.kernel.language.time.Duration;

/**
 * A condition lock allows thread A to ask thread B to satisfy a condition via {@link #request()}. Thread A can then
 * wait for thread B to satisfy the condition by calling {@link #waitFor(boolean)} . When thread B notices that {@link
 * #isRequested()} is true, it can begin to satisfy the condition requested from thread A. When it has successfully
 * completed the operation, it calls {@link #satisfy()} and thread A continues. The lock can be reset for re-use by
 * calling {@link #reset()} and the state of a lock can be queried at any time by calling {@link #isSatisfied()}.
 *
 * @author jonathanl (shibo)
 */
public class ConditionLock
{
    private volatile boolean requested;

    /** True when the condition is satisfied, false when it is not */
    private final BooleanLock satisfied;

    public ConditionLock(final BooleanLock satisfied)
    {
        this.satisfied = satisfied;
    }

    public boolean isRequested()
    {
        return requested;
    }

    public boolean isSatisfied()
    {
        return satisfied.get();
    }

    public void request()
    {
        requested = true;
    }

    public void reset()
    {
        requested = false;
        satisfied.set(false);
    }

    public void satisfy()
    {
        satisfied.set(true);
    }

    public void waitFor(final boolean condition)
    {
        waitFor(condition, Duration.MAXIMUM);
    }

    public boolean waitFor(final boolean condition, final Duration maximumWaitTime)
    {
        return satisfied.waitFor(condition, maximumWaitTime);
    }
}
