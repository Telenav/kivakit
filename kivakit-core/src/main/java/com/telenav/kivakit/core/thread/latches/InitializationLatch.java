////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.thread.latches;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A latch that waits for the initialization of something by another thread. The latch is waited on by one thread with
 * {@link #awaitInitialization(Duration)} and it is signaled by another thread via {@link #initializationComplete()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramThread.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class InitializationLatch
{
    private CountDownLatch countdown = new CountDownLatch(1);

    /**
     * Awaits initialization forever
     */
    public boolean awaitInitialization()
    {
        return awaitInitialization(Duration.MAXIMUM);
    }

    /**
     * Awaits initialization for the given duration
     */
    public boolean awaitInitialization(Duration duration)
    {
        try
        {
            return countdown.await(duration.milliseconds(), TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException ignored)
        {
        }
        return false;
    }

    /**
     * Called to indicate that initialization is complete
     */
    public void initializationComplete()
    {
        if (!isInitialized())
        {
            countdown.countDown();
        }
    }

    /**
     * True if initialization is completed
     */
    public boolean isInitialized()
    {
        return countdown.getCount() == 0;
    }

    /**
     * Resets this latch for another use
     */
    public void reset()
    {
        countdown = new CountDownLatch(1);
    }
}
