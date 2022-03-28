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

package com.telenav.kivakit.core.time;
import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

public class RateTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        var rate = Rate.perSecond(1);
        ensureEqual(1.0, rate.count());
        ensureEqual(60.0, rate.perMinute().count());
        ensureEqual(3600.0, rate.perHour().count());
        ensureEqual(24 * 3600.0, rate.perDay().count());
        ensureEqual(Rate.perMinute(60.0), rate);
        ensure(rate.compareTo(Rate.perSecond(2)) < 0);
        ensure(rate.isSlowerThan(Rate.perSecond(2)));
        ensure(rate.isFasterThan(Rate.perSecond(0.5)));
    }
}
