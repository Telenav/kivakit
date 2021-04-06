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

package com.telenav.kivakit.core.collections.primitive.map.multi.dynamic;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import org.junit.Test;

public class LongToLongMultiMapTest extends CoreCollectionsUnitTest
{
    @Test
    public void test()
    {
        final var map = new LongToLongMultiMap("test");
        map.initialize();
        for (long i = 0; i < 100; i++)
        {
            for (long j = 0; j < 10; j++)
            {
                map.add(i, j);
            }
        }
        for (long i = 0; i < 100; i++)
        {
            final var values = map.get(i);
            for (long j = 0; j < 10; j++)
            {
                // values are added to the list in reverse order
                ensureEqual(j, values.get((int) (9 - j)));
            }
            final var iterator = map.iterator(i);
            if (iterator != null)
            {
                long expected = 9;
                while (iterator.hasNext())
                {
                    final var value = iterator.next();
                    ensureEqual(expected--, value);
                }
            }
        }
    }
}
