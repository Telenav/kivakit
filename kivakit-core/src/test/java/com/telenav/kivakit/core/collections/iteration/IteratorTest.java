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

package com.telenav.kivakit.core.collections.iteration;

import com.telenav.kivakit.core.collections.list.BaseList;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.NextValue;
import org.junit.Test;

import java.util.Collections;

public class IteratorTest extends UnitTest
{
    @Test
    public void abstractIterableTest()
    {
        var iterable = Iterables.iterable(() -> new
                NextValue<Integer>()
                {
                    int i;

                    int n = 1;

                    @Override
                    public Integer next()
                    {
                        if (i++ < 10)
                        {
                            n += n;
                            return n;
                        }
                        return null;
                    }
                });
        ensureEqual(values(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024), new ObjectList<>().appendAll(iterable));
        ensureEqual(values(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024), new ObjectList<>().appendAll(iterable));
    }

    @Test
    public void abstractIteratorTest()
    {
        BaseIterator<Integer> iterator = new BaseIterator<>()
        {
            int i;

            int n = 1;

            @Override
            protected Integer onNext()
            {
                if (i++ < 10)
                {
                    n += n;
                    return n;
                }
                return null;
            }
        };
        ensureEqual(values(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024), new ObjectList<>().appendAll(iterator));
    }

    @Test
    public void compoundIteratorTest()
    {
        var odd = values(1, 3, 5, 7, 9);
        var even = values(2, 4, 6, 8, 10);
        var both = new CompoundIterator<Integer>();
        both.add(odd.iterator());
        both.add(even.iterator());
        var compound = new ObjectList<Integer>(Maximum.MAXIMUM).appendAll(both);
        ensureEqual(values(1, 3, 5, 7, 9, 2, 4, 6, 8, 10), compound);
    }

    @Test
    public void deduplicatedIteratorTest()
    {
        var values = values(2, 3, 2, 5, 2, 7, 2);
        var iterator = new DeduplicatingIterator<>(values.iterator());
        BaseList<Integer> deduplicated = new ObjectList<Integer>(Maximum.MAXIMUM).appendAll(iterator);
        ensureEqual(values(2, 3, 5, 7), deduplicated);
    }

    private ObjectList<Integer> values(Integer... values)
    {
        var list = new ObjectList<Integer>();
        Collections.addAll(list, values);
        return list;
    }
}
