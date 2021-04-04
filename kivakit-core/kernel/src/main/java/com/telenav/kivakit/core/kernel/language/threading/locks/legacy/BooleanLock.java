////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.locks.legacy;

import com.telenav.kivakit.core.kernel.language.time.Duration;

public interface BooleanLock
{
    boolean get();

    void set(final boolean state);

    default void waitFor(final boolean state)
    {
        waitFor(state, Duration.MAXIMUM);
    }

    /**
     * @param state The state to wait for
     * @param maximumWaitTime The maximum amount of time to wait
     * @return True if the condition was satisfied and false if the maximum wait time elapsed
     */
    boolean waitFor(final boolean state, final Duration maximumWaitTime);
}
