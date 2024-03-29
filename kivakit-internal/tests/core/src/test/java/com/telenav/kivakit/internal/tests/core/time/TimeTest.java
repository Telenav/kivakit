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

package com.telenav.kivakit.internal.tests.core.time;

import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.ONE_DAY;
import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.core.time.TimeZones.utc;

/**
 * Time test
 */
public class TimeTest extends CoreUnitTest
{
    @Test
    public void testBeforeAfter()
    {
        var now = now();
        var later = now.plus(ONE_SECOND);
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
        var now = now();
        var later = now.plus(ONE_SECOND);
        ensureEqual(now, now.minimum(later));
        ensureEqual(later, now.maximum(later));
        ensureEqual(now, later.minimum(now));
        ensureEqual(later, later.maximum(now));
    }

    @Test
    public void testMinus()
    {
        var now = now().roundDown(ONE_SECOND);
        var later = now.plus(ONE_SECOND);
        ensureEqual(ONE_SECOND, later.minus(now));
    }

    @Test
    public void testParse()
    {
        var text = "2023.01.16_07.56PM_UTC";
        var time = Time.time(text);
        var localTime = time.asLocalTime(utc());
        ensureEqual(localTime.toString(), text);
    }

    @Test
    public void testPlus()
    {
        var now = now().roundDown(ONE_SECOND);
        var earlier = now.minus(ONE_SECOND);

        ensureEqual(now, earlier.plus(ONE_SECOND));
    }

    @Test
    public void testRoundDown()
    {
        var now = now();
        var thisSecond = now.roundDown(ONE_SECOND);
        ensure(thisSecond.isAtOrBefore(now));
    }

    @Test
    public void testStartOfToday()
    {
        var startOfToday = now().asLocalTime().startOfDay();
        ensure(now().minus(startOfToday).isLessThanOrEqualTo(ONE_DAY));
    }
}
