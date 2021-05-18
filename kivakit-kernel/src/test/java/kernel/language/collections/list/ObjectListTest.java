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

package kernel.language.collections.list;

import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.values.count.Count;
import org.junit.Assert;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

public class ObjectListTest
{
    @SuppressWarnings("CollectionAddedToSelf")
    @Test
    public void testEndsWith()
    {
        final ObjectList<Integer> list = createList(0, 4);
        final ObjectList<Integer> validEnd = createList(3, 4);
        final ObjectList<Integer> invalidEnd1 = createList(1, 3);
        final ObjectList<Integer> invalidEnd2 = createList(0, 5);

        Assert.assertTrue(list.endsWith(list));
        Assert.assertTrue(list.endsWith(validEnd));
        Assert.assertFalse(list.endsWith(invalidEnd1));
        Assert.assertFalse(list.endsWith(invalidEnd2));
        Assert.assertFalse(list.endsWith(null));
    }

    @Test
    public void testPartition()
    {
        final ObjectList<Integer> list = createList(0, 10);
        ensure(list.partition(Count._3).size() == 3);
        ensure(list.partition(Count._4).size() == 4);
        ensure(list.partition(Count._5).size() == 5);
        ensure(list.partition(Count._6).size() == 6);
    }

    @SuppressWarnings("CollectionAddedToSelf")
    @Test
    public void testStartsWith()
    {
        final ObjectList<Integer> list = createList(0, 4);
        final ObjectList<Integer> validStart = createList(0, 2);
        final ObjectList<Integer> invalidStart1 = createList(1, 4);
        final ObjectList<Integer> invalidStart2 = createList(0, 5);

        Assert.assertTrue(list.startsWith(list));
        Assert.assertTrue(list.startsWith(validStart));
        Assert.assertFalse(list.startsWith(invalidStart1));
        Assert.assertFalse(list.startsWith(invalidStart2));
        Assert.assertFalse(list.startsWith(null));
    }

    private ObjectList<Integer> createList(final int firstValue, final int lastValue)
    {
        final ObjectList<Integer> list = new ObjectList<>();
        for (var i = firstValue; i <= lastValue; i++)
        {
            list.add(i);
        }
        return list;
    }
}
