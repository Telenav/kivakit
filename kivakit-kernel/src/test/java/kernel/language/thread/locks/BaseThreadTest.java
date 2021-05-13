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

package kernel.language.thread.locks;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;

/**
 * @author jonathanl (shibo)
 */
public abstract class BaseThreadTest
{
    private Time start;

    public void run()
    {
        var retries = 0;
        var pause = Duration.ONE_SECOND;
        while (true)
        {
            start = Time.now();
            try
            {
                onRun();
                break;
            }
            catch (final Exception e)
            {
                if (retries++ > 10)
                {
                    Ensure.fail("OS scheduling is pathological");
                }
                pause.sleep();
                pause = pause.times(2);
            }
        }
    }

    protected void accurateSleep(final Duration minimum, final Duration maximum)
    {
        final var start = Time.now();
        minimum.sleep();
        Ensure.ensure(start.elapsedSince().isLessThanOrEqualTo(maximum));
    }

    protected void ensureElapsedGreaterThan(final Duration minimum)
    {
        ensureElapsedGreaterThan(start, minimum);
    }

    protected void ensureElapsedGreaterThan(final Time start, final Duration minimum)
    {
        Ensure.ensure(start.elapsedSince().isGreaterThan(minimum));
    }

    protected void ensureElapsedLessThan(final Duration maximum)
    {
        ensureElapsedLessThan(start, maximum);
    }

    protected void ensureElapsedLessThan(final Time start, final Duration maximum)
    {
        Ensure.ensure(start.elapsedSince().isLessThan(maximum));
    }

    protected Duration maximumWait()
    {
        return Duration.seconds(3);
    }

    protected abstract void onRun();
}
