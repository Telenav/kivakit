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

package kernel.language.time;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureFalse;

/**
 * Time test
 */
public class TimeTest
{
    @Test
    public void testBeforeAfter()
    {
        var now = Time.now();
        var later = now.plus(Duration.ONE_SECOND);
        ensure(now.isBefore(later));
        ensure(now.isAtOrBefore(later));
        ensure(now.isAtOrBefore(now));
        ensureFalse(now.isAfter(later));
        ensureFalse(now.isAtOrAfter(later));
        ensure(later.isAtOrAfter(later));
    }

    @Test
    public void testMinimumMaximum()
    {
        var now = Time.now();
        var later = now.plus(Duration.ONE_SECOND);
        ensureEqual(now, now.minimum(later));
        ensureEqual(later, now.maximum(later));
        ensureEqual(now, later.minimum(now));
        ensureEqual(later, later.maximum(now));
    }

    @Test
    public void testRoundDown()
    {
        var now = Time.now();
        var thisSecond = now.roundDown(Duration.ONE_SECOND);
        ensure(thisSecond.isAtOrBefore(now));
    }

    @Test
    public void testStartOfToday()
    {
        Time startOfToday = Time.now().localTime().startOfDay();
        ensure(Time.now().minus(startOfToday).isLessThanOrEqualTo(Duration.ONE_DAY));
    }

    @Test
    public void testSubtract()
    {
        var now = Time.now();
        var later = now.plus(Duration.ONE_SECOND);
        ensureEqual(Duration.ONE_SECOND, later.minus(now));
    }
}
