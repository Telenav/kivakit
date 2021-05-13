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

package com.telenav.kivakit.collections.iteration.iterators;

import com.telenav.kivakit.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.kernel.language.collections.list.BaseList;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.iteration.BaseIterator;
import com.telenav.kivakit.kernel.language.iteration.Iterables;
import com.telenav.kivakit.kernel.language.iteration.Next;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import org.junit.Test;

import java.util.Collections;

public class IteratorTest extends CoreCollectionsUnitTest
{
    @Test
    public void abstractIterableTest()
    {
        final var iterable = Iterables.iterable(() -> new Next<Integer>()
        {
            int i;

            int n = 1;

            @Override
            public Integer onNext()
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
        final BaseIterator<Integer> iterator = new BaseIterator<>()
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
        final var odd = values(1, 3, 5, 7, 9);
        final var even = values(2, 4, 6, 8, 10);
        final var both = new CompoundIterator<Integer>();
        both.add(odd.iterator());
        both.add(even.iterator());
        final var compound = new ObjectList<Integer>(Maximum.MAXIMUM).appendAll(both);
        ensureEqual(values(1, 3, 5, 7, 9, 2, 4, 6, 8, 10), compound);
    }

    @Test
    public void deduplicatedIteratorTest()
    {
        final var values = values(2, 3, 2, 5, 2, 7, 2);
        final var iterator = new DeduplicatingIterator<>(values.iterator());
        final BaseList<Integer> deduplicated = new ObjectList<Integer>(Maximum.MAXIMUM).appendAll(iterator);
        ensureEqual(values(2, 3, 5, 7), deduplicated);
    }

    private ObjectList<Integer> values(final Integer... values)
    {
        final var list = new ObjectList<Integer>();
        Collections.addAll(list, values);
        return list;
    }
}
