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

package kernel.time;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Frequency.Cycle;
import com.telenav.kivakit.core.kernel.language.time.Time;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureWithin;

public class FrequencyTest
{
    @Test
    public void test()
    {
        final Frequency frequency = Frequency.EVERY_10_SECONDS;
        final var now = Time.now();
        final Cycle cycle = frequency.start(now);
        ensureWithin(Duration.seconds(10).asSeconds(), cycle.waitTimeBeforeNextCycle().asSeconds(), 1.0);
        ensureWithin(now.plus(Duration.seconds(10)).asSeconds(), cycle.next().asSeconds(), 1.0);
    }
}
