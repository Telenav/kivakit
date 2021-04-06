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

package kernel.language.thread;

import com.telenav.kivakit.core.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.mutable.MutableValue;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class KivaKitThreadTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Test
    public void testInitialDelay()
    {
        final var executed = new MutableValue<Time>();
        final KivaKitThread thread = new KivaKitThread("test")
        {
            @Override
            protected void onRun()
            {
                executed.set(Time.now());
            }
        };

        thread.addListener(LOGGER);
        thread.initialDelay(Duration.milliseconds(50));
        thread.startSynchronously();
        ensure(thread.isRunning());
        thread.join();
        ensure(thread.startedAt().elapsedSince().isApproximately(Duration.milliseconds(50), Duration.seconds(0.5)));
        ensure(executed.get().elapsedSince().isApproximately(Duration.NONE, Duration.seconds(0.1)));
    }

    @Test
    public void testOnExecute()
    {
        final var executed = new MutableValue<Boolean>();
        final KivaKitThread thread = new KivaKitThread("test")
        {
            @Override
            protected void onRun()
            {
                executed.set(true);
            }
        };
        thread.addListener(LOGGER);
        thread.start();
        thread.join();
        ensure(executed.get());
    }

    @Test
    public void testOnXXX()
    {
        final var at = new MutableValue<Integer>();
        at.set(0);
        final KivaKitThread thread = new KivaKitThread("test")
        {
            @Override
            protected void onAfter()
            {
                ensureEqual(2, at.get());
                at.set(3);
            }

            @Override
            protected void onBefore()
            {
                ensureEqual(0, at.get());
                at.set(1);
            }

            @Override
            protected void onRun()
            {
                ensureEqual(1, at.get());
                at.set(2);
            }
        };
        thread.addListener(LOGGER);
        thread.start();
        thread.join();
        ensureEqual(3, at.get());
    }
}
