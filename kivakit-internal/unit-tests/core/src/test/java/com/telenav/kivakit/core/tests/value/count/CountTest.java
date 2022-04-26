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

package com.telenav.kivakit.core.tests.value.count;
import com.telenav.kivakit.core.test.support.CoreUnitTest;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.level.Percent;
import org.junit.Test;

public class CountTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        final Count count = Count.count(99);
        ensureEqual(99, count.asInt());
        ensureEqual(Count.count(100), count.plus(Count._1));
        ensureEqual(Count.count(100), count.plus(1));
        ensureEqual(Count.count(100), count.incremented());
        ensureFalse(count.isZero());
        ensure(Count._0.isZero());
        ensureEqual(Percent.of(99), count.percentOf(Count.count(100)));
        ensureEqual(-1, count.compareTo(Count.count(100)));
        //noinspection EqualsWithItself
        ensureEqual(0, count.compareTo(count));
        ensureEqual(1, count.compareTo(Count.count(98)));
    }
}
