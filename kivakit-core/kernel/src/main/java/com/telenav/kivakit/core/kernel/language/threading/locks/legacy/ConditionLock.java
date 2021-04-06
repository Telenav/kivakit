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
