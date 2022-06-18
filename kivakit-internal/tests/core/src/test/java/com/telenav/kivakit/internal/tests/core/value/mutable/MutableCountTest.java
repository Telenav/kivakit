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

package com.telenav.kivakit.internal.tests.core.value.mutable;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.MutableCount;
import org.junit.Test;

public class MutableCountTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        var count = new MutableCount();
        ensureEqual(0L, count.asLong());
        ensureEqual(Count._0, count.count());
        count.increment();
        ensureEqual(1L, count.asLong());
        ensureEqual(Count._1, count.asCount());
        count.plus(1);
        ensureEqual(2L, count.asLong());
        count.clear();
        ensureEqual(0L, count.asLong());
        ensureEqual(Count._0, count.asCount());
        ensure(count.equals(new MutableCount()));
        ensureFalse(count.isGreaterThan(new MutableCount()));
        ensureFalse(count.isLessThan(new MutableCount()));
    }
}
