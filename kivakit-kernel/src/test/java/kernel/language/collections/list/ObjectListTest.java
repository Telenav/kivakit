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

import java.util.Comparator;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ObjectListTest
{
    @Test
    public void testAdd()
    {
        final var list = ObjectList.objectList(1, 2, 3, 4, 5);
        list.add(9);
        ensureEqual(list, ObjectList.objectList(1, 2, 3, 4, 5, 9));
    }

    @Test
    public void testAllMatch()
    {
        ensure(ObjectList.objectList(1, 2, 3, 4, 5).allMatch(value -> value < 6));
    }

    @Test
    public void testAnyMatch()
    {
        ensure(ObjectList.objectList(1, 2, 3, 4, 5).anyMatch(value -> value == 4));
    }

    @Test
    public void testAppend()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).append(7).append(8), ObjectList.objectList(1, 2, 3, 4, 5, 7, 8));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).appendAll(List.of(7, 8)), ObjectList.objectList(1, 2, 3, 4, 5, 7, 8));
    }

    @Test
    public void testCopy()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).copy(), ObjectList.objectList(1, 2, 3, 4, 5));
    }

    @SuppressWarnings("CollectionAddedToSelf")
    @Test
    public void testEndsWith()
    {
        final var list = createList(0, 4);
        final var validEnd = createList(3, 4);
        final var invalidEnd1 = createList(1, 3);
        final var invalidEnd2 = createList(0, 5);

        Assert.assertTrue(list.endsWith(list));
        Assert.assertTrue(list.endsWith(validEnd));
        Assert.assertFalse(list.endsWith(invalidEnd1));
        Assert.assertFalse(list.endsWith(invalidEnd2));
        Assert.assertFalse(list.endsWith(null));
    }

    @Test
    public void testEquals()
    {
        ensure(ObjectList.objectList(1, 2, 3, 4, 5).equals(ObjectList.objectList(1, 2, 3, 4, 5)));
    }

    @Test
    public void testFind()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).find(value -> value == 3), 3);
    }

    @Test
    public void testFirst()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).first(), 1);
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).first(3), ObjectList.objectList(1, 2, 3));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).first(2), ObjectList.objectList(1, 2));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).first(1), ObjectList.objectList(1));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).first(0), ObjectList.objectList());
    }

    @Test
    public void testGet()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).get(0), 1);
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).get(2), 3);
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).get(4), 5);
    }

    @Test
    public void testHead()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).head(), 1);
    }

    @Test
    public void testIsEmpty()
    {
        ensure(!ObjectList.objectList(1, 2, 3, 4, 5).isEmpty());
        ensure(ObjectList.objectList().isEmpty());
    }

    @Test
    public void testIsNonEmpty()
    {
        ensure(ObjectList.objectList(1, 2, 3, 4, 5).isNonEmpty());
        ensure(!ObjectList.objectList().isNonEmpty());
    }

    @Test
    public void testJoin()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).join(), "1, 2, 3, 4, 5");
    }

    @Test
    public void testLastIndexOf()
    {
        ensureEqual(ObjectList.objectList(3, 2, 3, 4, 3).lastIndexOf(3), 4);
        ensureEqual(ObjectList.objectList(3, 2, 3, 4, 9).lastIndexOf(3), 2);
        ensureEqual(ObjectList.objectList(3, 2, 9, 4, 9).lastIndexOf(3), 0);
        ensureEqual(ObjectList.objectList(9, 2, 9, 4, 9).lastIndexOf(3), -1);
    }

    @Test
    public void testLeftOf()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).leftOf(2), ObjectList.objectList(1, 2));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).leftOf(1), ObjectList.objectList(1));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).leftOf(0), ObjectList.objectList());
    }

    @Test
    public void testMapped()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).mapped(value -> value * value), ObjectList.objectList(1, 4, 9, 16, 25));
    }

    @Test
    public void testMatching()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).matching(value -> value % 2 == 1), ObjectList.objectList(1, 3, 5));
    }

    @Test
    public void testMaybeReversed()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).maybeReversed(true), ObjectList.objectList(5, 4, 3, 2, 1));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).maybeReversed(false), ObjectList.objectList(1, 2, 3, 4, 5));
    }

    @Test
    public void testPartition()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).partition(Count._2), ObjectList.objectList(ObjectList.objectList(1, 2, 3), ObjectList.objectList(4, 5)));

        final var list = createList(0, 10);
        ensure(list.partition(Count._3).size() == 3);
        ensure(list.partition(Count._4).size() == 4);
        ensure(list.partition(Count._5).size() == 5);
        ensure(list.partition(Count._6).size() == 6);
    }

    @Test
    public void testPrepend()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).prepend(6), ObjectList.objectList(6, 1, 2, 3, 4, 5));
    }

    @Test
    public void testQuantized()
    {
        ensureEqual(ObjectList.objectList(Count._1, Count._5, Count._7).quantized(), new long[] { 1L, 5L, 7L });
    }

    @Test
    public void testRemoveLast()
    {
        final var list = ObjectList.objectList(1, 2, 3, 4, 5);
        var removed = list.removeLast();
        ensureEqual(removed, 5);
        ensureEqual(list, ObjectList.objectList(1, 2, 3, 4));
    }

    @Test
    public void testReversed()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).reversed(), ObjectList.objectList(5, 4, 3, 2, 1));
    }

    @Test
    public void testRightOf()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).rightOf(2), ObjectList.objectList(4, 5));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).rightOf(1), ObjectList.objectList(3, 4, 5));
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).rightOf(0), ObjectList.objectList(2, 3, 4, 5));
    }

    @Test
    public void testSet()
    {
        final var list = ObjectList.objectList(1, 2, 3, 4, 5);
        list.set(0, 9);
        ensureEqual(list, ObjectList.objectList(9, 2, 3, 4, 5));
        list.set(2, 9);
        ensureEqual(list, ObjectList.objectList(9, 2, 9, 4, 5));
        list.set(4, 9);
        ensureEqual(list, ObjectList.objectList(9, 2, 9, 4, 9));
    }

    @Test
    public void testSize()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).size(), 5);
        ensureEqual(ObjectList.objectList().size(), 0);
    }

    @Test
    public void testSorted()
    {
        ensureEqual(ObjectList.objectList(5, 4, 2, 1, 3).sorted(), ObjectList.objectList(1, 2, 3, 4, 5));
        ensureEqual(ObjectList.objectList(5, 4, 2, 1, 3).sorted(Comparator.reverseOrder()), ObjectList.objectList(5, 4, 3, 2, 1));
    }

    @SuppressWarnings("CollectionAddedToSelf")
    @Test
    public void testStartsWith()
    {
        final var list = createList(0, 4);
        final var validStart = createList(0, 2);
        final var invalidStart1 = createList(1, 4);
        final var invalidStart2 = createList(0, 5);

        Assert.assertTrue(list.startsWith(list));
        Assert.assertTrue(list.startsWith(validStart));
        Assert.assertFalse(list.startsWith(invalidStart1));
        Assert.assertFalse(list.startsWith(invalidStart2));
        Assert.assertFalse(list.startsWith(null));
    }

    @Test
    public void testTail()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3, 4, 5).tail(), ObjectList.objectList(2, 3, 4, 5));
    }

    @Test
    public void testUnique()
    {
        ensureEqual(ObjectList.objectList(1, 2, 3), ObjectList.objectList(1, 2, 3, 1, 2, 3).uniqued());
    }

    @Test
    public void testWith()
    {
        final var list = createList(0, 4);
        ensureEqual(list.with(5), createList(0, 5));
    }

    @Test
    public void testWithout()
    {
        ensureEqual(createList(0, 4).without(value -> value == 4), createList(0, 3));
        ensureEqual(ObjectList.objectList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).without(value -> value % 2 == 0), ObjectList.objectList(1, 3, 5, 7, 9));
    }

    private ObjectList<Integer> createList(final int firstValue, final int lastValue)
    {
        final var list = new ObjectList<Integer>();
        for (var i = firstValue; i <= lastValue; i++)
        {
            list.add(i);
        }
        return list;
    }
}
