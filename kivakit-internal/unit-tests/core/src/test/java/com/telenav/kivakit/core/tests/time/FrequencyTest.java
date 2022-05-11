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

package com.telenav.kivakit.core.tests.time;

import com.telenav.kivakit.core.test.support.CoreUnitTest;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.ONE_DAY;
import static com.telenav.kivakit.core.time.Duration.hours;
import static com.telenav.kivakit.core.time.Duration.minutes;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Frequency.EVERY_10_SECONDS;
import static com.telenav.kivakit.core.time.Frequency.EVERY_30_SECONDS;
import static com.telenav.kivakit.core.time.Frequency.EVERY_5_SECONDS;
import static com.telenav.kivakit.core.time.Frequency.EVERY_HALF_SECOND;
import static com.telenav.kivakit.core.time.Frequency.EVERY_MINUTE;
import static com.telenav.kivakit.core.time.Frequency.cyclesPerDay;
import static com.telenav.kivakit.core.time.Frequency.cyclesPerHour;
import static com.telenav.kivakit.core.time.Frequency.cyclesPerMinute;
import static com.telenav.kivakit.core.time.Frequency.cyclesPerSecond;
import static com.telenav.kivakit.core.time.Frequency.every;
import static com.telenav.kivakit.core.time.Frequency.parseFrequency;

/**
 * Tests for {@link Frequency}
 *
 * @author jonathanl (shibo)
 */
public class FrequencyTest extends CoreUnitTest
{
    @Test
    public void testCreationAndEquals()
    {
        ensureEqual(every(seconds(5)), EVERY_5_SECONDS);
        ensureEqual(cyclesPerDay(5), every(ONE_DAY.dividedBy(5)));
        ensureEqual(cyclesPerSecond(10), every(seconds(0.1)));
        ensureEqual(cyclesPerMinute(10), every(minutes(0.1)));
        ensureEqual(cyclesPerHour(10), every(hours(0.1)));
    }

    @Test
    public void testCycleTiming()
    {
        var now = Time.now();
        var cycle = EVERY_10_SECONDS.start(now);
        ensureWithin(seconds(10).asSeconds(), cycle.waitTimeBeforeNextCycle().asSeconds(), 1.0);
        ensureWithin(now.plus(seconds(10)).asSeconds(), cycle.next().asSeconds(), 1.0);
    }

    @Test
    public void testParse()
    {
        ensureThrows(() -> parseFrequency(this, "5"));
        ensureThrows(() -> parseFrequency(this, "5 cows"));
        ensureThrows(() -> parseFrequency(this, "every"));
        ensureThrows(() -> parseFrequency(this, null));
        ensureThrows(() -> parseFrequency(this, ""));

        ensureEqual(parseFrequency(this, "every 5 seconds"), EVERY_5_SECONDS);
        ensureEqual(parseFrequency(this, "every minute"), EVERY_MINUTE);
        ensureEqual(parseFrequency(this, "every 30s"), EVERY_30_SECONDS);
        ensureEqual(parseFrequency(this, "every 500ms"), EVERY_HALF_SECOND);
    }

    @Test
    public void testToString()
    {
        ensureEqual(EVERY_10_SECONDS.toString(), "every 10 seconds");
    }
}
