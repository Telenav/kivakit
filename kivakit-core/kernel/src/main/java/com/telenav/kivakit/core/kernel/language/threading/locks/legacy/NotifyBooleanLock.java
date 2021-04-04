////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.locks.legacy;

public class NotifyBooleanLock extends NotifyAllBooleanLock
{
    public NotifyBooleanLock()
    {
    }

    public NotifyBooleanLock(final boolean state)
    {
        super(state);
    }

    @Override
    public synchronized void set(final boolean state)
    {
        if (state != this.state)
        {
            this.state = state;
            notify();
        }
    }
}
