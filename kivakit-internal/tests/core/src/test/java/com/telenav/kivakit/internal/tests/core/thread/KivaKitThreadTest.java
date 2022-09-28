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

package com.telenav.kivakit.internal.tests.core.thread;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.mutable.MutableValue;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.thread.KivaKitThread.State.EXITED;
import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;
import static com.telenav.kivakit.core.time.Duration.milliseconds;
import static com.telenav.kivakit.core.time.Duration.seconds;

public class KivaKitThreadTest extends CoreUnitTest
{
    @Test
    public void testInitialDelay()
    {
        var executedAt = new MutableValue<Time>();
        KivaKitThread thread = Listener.emptyListener().listenTo(new KivaKitThread("Test")
        {
            @Override
            protected void onRun()
            {
                executedAt.set(Time.now());
            }
        });

        thread.addListener(this);
        thread.initialDelay(milliseconds(50));
        thread.startSynchronously();
        thread.waitFor(EXITED);
        ensure(thread.stateMachine().is(EXITED));
        ensure(thread.startedAt().elapsedSince().isCloseTo(milliseconds(50), seconds(0.5)));
        ensure(executedAt.get().elapsedSince().isCloseTo(ZERO_DURATION, seconds(0.1)));
    }

    @Test
    public void testLifecycle()
    {
        var at = new MutableValue<Integer>();
        at.set(0);
        KivaKitThread thread = new KivaKitThread("test")
        {
            @Override
            protected void onRan()
            {
                super.onRan();
                ensureEqual(2, at.get());
                at.set(3);
            }

            @Override
            protected void onRun()
            {
                ensureEqual(1, at.get());
                at.set(2);
            }

            @Override
            protected void onRunning()
            {
                super.onRunning();
                ensureEqual(0, at.get());
                at.set(1);
            }
        };
        thread.addListener(this);
        thread.start();
        thread.join();
        ensureEqual(3, at.get());
    }

    @Test
    public void testOnExecute()
    {
        var executed = new MutableValue<Boolean>();
        KivaKitThread thread = new KivaKitThread("test")
        {
            @Override
            protected void onRun()
            {
                executed.set(true);
            }
        };
        thread.addListener(this);
        thread.start();
        thread.join();
        ensure(executed.get());
    }
}
