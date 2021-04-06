////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
