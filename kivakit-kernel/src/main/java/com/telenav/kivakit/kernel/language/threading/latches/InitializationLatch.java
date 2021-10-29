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

package com.telenav.kivakit.kernel.language.threading.latches;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A latch that waits for the initialization of something by another thread. The latch is waited on by one thread with
 * {@link #await(Duration)} and it is signaled by another thread via {@link #ready()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class InitializationLatch
{
    private CountDownLatch countdown = new CountDownLatch(1);

    public boolean await(Duration duration)
    {
        try
        {
            return countdown.await(duration.asMilliseconds(), TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException ignored)
        {
        }
        return false;
    }

    public boolean isReady()
    {
        return countdown.getCount() == 0;
    }

    public void ready()
    {
        if (!isReady())
        {
            countdown.countDown();
        }
    }

    public void reset()
    {
        countdown = new CountDownLatch(1);
    }
}
