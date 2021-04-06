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

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

public class MutableCountTest
{
    @Test
    public void test()
    {
        final var count = new MutableCount();
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
