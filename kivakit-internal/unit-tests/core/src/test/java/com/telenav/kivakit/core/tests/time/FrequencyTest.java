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
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import org.junit.Test;

public class FrequencyTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        Frequency frequency = Frequency.EVERY_10_SECONDS;
        var now = Time.now();
        var cycle = frequency.start(now);
        ensureWithin(Duration.seconds(10).asSeconds(), cycle.waitTimeBeforeNextCycle().asSeconds(), 1.0);
        ensureWithin(now.plus(Duration.seconds(10)).asSeconds(), cycle.next().asSeconds(), 1.0);
    }
}
