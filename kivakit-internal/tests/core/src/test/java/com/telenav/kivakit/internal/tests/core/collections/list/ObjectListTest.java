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

package com.telenav.kivakit.internal.tests.core.collections.list;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class ObjectListTest extends CoreUnitTest
{
    @Test
    public void testAdd()
    {
        var list = list(1, 2, 3, 4, 5);
        list.add(9);
        ensureEqual(list, list(1, 2, 3, 4, 5, 9));
    }

    @Test
    public void testAllMatch()
    {
        ensure(list(1, 2, 3, 4, 5).allMatch(value -> value < 6));
    }

    @Test
    public void testAnyMatch()
    {
        ensure(list(1, 2, 3, 4, 5).anyMatch(value -> value == 4));
    }

    @Test
    public void testAppend()
    {
        ensureEqual(list(1, 2, 3, 4, 5).with(7).with(8),
                list(1, 2, 3, 4, 5, 7, 8));
        ensureEqual(list(1, 2, 3, 4, 5).appendAllThen(List.of(7, 8)), list(1, 2, 3, 4, 5, 7, 8));
    }

    @Test
    public void testCopy()
    {
        ensureEqual(list(1, 2, 3, 4, 5).copy(), list(1, 2, 3, 4, 5));
    }

    @Test
    public void testEndsWith()
    {
        var list = createList(0, 4);
        var validEnd = createList(3, 4);
        var invalidEnd1 = createList(1, 3);
        var invalidEnd2 = createList(0, 5);

        Assert.assertTrue(list.endsWith(list));
        Assert.assertTrue(list.endsWith(validEnd));
        Assert.assertFalse(list.endsWith(invalidEnd1));
        Assert.assertFalse(list.endsWith(invalidEnd2));
        Assert.assertFalse(list.endsWith(null));
    }

    @Test
    public void testEquals()
    {
        ensure(list(1, 2, 3, 4, 5).equals(list(1, 2, 3, 4, 5)));
    }

    @Test
    public void testFind()
    {
        ensureEqual(list(1, 2, 3, 4, 5).findFirst(value -> value == 3), 3);
    }

    @Test
    public void testFirst()
    {
        ensureEqual(list(1, 2, 3, 4, 5).first(), 1);
        ensureEqual(list(1, 2, 3, 4, 5).first(3), list(1, 2, 3));
        ensureEqual(list(1, 2, 3, 4, 5).first(2), list(1, 2));
        ensureEqual(list(1, 2, 3, 4, 5).first(1), list(1));
        ensureEqual(list(1, 2, 3, 4, 5).first(0), list());
    }

    @Test
    public void testGet()
    {
        ensureEqual(list(1, 2, 3, 4, 5).get(0), 1);
        ensureEqual(list(1, 2, 3, 4, 5).get(2), 3);
        ensureEqual(list(1, 2, 3, 4, 5).get(4), 5);
    }

    @Test
    public void testHead()
    {
        ensureEqual(list(1, 2, 3, 4, 5).head(), 1);
    }

    @Test
    public void testIsEmpty()
    {
        ensure(!list(1, 2, 3, 4, 5).isEmpty());
        ensure(list().isEmpty());
    }

    @Test
    public void testIsNonEmpty()
    {
        ensure(list(1, 2, 3, 4, 5).isNonEmpty());
        ensure(!list().isNonEmpty());
    }

    @Test
    public void testJoin()
    {
        ensureEqual(list(1, 2, 3, 4, 5).join(), "1, 2, 3, 4, 5");
    }

    @Test
    public void testLastIndexOf()
    {
        ensureEqual(list(3, 2, 3, 4, 3).lastIndexOf(3), 4);
        ensureEqual(list(3, 2, 3, 4, 9).lastIndexOf(3), 2);
        ensureEqual(list(3, 2, 9, 4, 9).lastIndexOf(3), 0);
        ensureEqual(list(9, 2, 9, 4, 9).lastIndexOf(3), -1);
    }

    @Test
    public void testLeftOf()
    {
        ensureEqual(list(1, 2, 3, 4, 5).leftOf(2), list(1, 2));
        ensureEqual(list(1, 2, 3, 4, 5).leftOf(1), list(1));
        ensureEqual(list(1, 2, 3, 4, 5).leftOf(0), list());
    }

    @Test
    public void testMapped()
    {
        ensureEqual(list(1, 2, 3, 4, 5).map(value -> value * value), list(1, 4, 9, 16, 25));
    }

    @Test
    public void testMatching()
    {
        ensureEqual(list(1, 2, 3, 4, 5).matching(value -> value % 2 == 1), list(1, 3, 5));
    }

    @Test
    public void testMaybeReversed()
    {
        ensureEqual(list(1, 2, 3, 4, 5).maybeReversed(true), list(5, 4, 3, 2, 1));
        ensureEqual(list(1, 2, 3, 4, 5).maybeReversed(false), list(1, 2, 3, 4, 5));
    }

    @Test
    public void testPartition()
    {
        ensureEqual(list(1, 2, 3, 4, 5).partition(Count._2), list(list(1, 2, 3), list(4, 5)));

        var list = createList(0, 10);
        ensure(list.partition(Count._3).size() == 3);
        ensure(list.partition(Count._4).size() == 4);
        ensure(list.partition(Count._5).size() == 5);
        ensure(list.partition(Count._6).size() == 6);
    }

    @Test
    public void testPrepend()
    {
        var list = list(1, 2, 3, 4, 5);
        list.prepend(6);
        ensureEqual(list, list(6, 1, 2, 3, 4, 5));
    }

    @Test
    public void testRemoveLast()
    {
        var list = list(1, 2, 3, 4, 5);
        var removed = list.removeLast();
        ensureEqual(removed, 5);
        ensureEqual(list, list(1, 2, 3, 4));
    }

    @Test
    public void testReversed()
    {
        ensureEqual(list(1, 2, 3, 4, 5).reversed(), list(5, 4, 3, 2, 1));
    }

    @Test
    public void testRightOf()
    {
        ensureEqual(list(1, 2, 3, 4, 5).rightOf(2), list(4, 5));
        ensureEqual(list(1, 2, 3, 4, 5).rightOf(1), list(3, 4, 5));
        ensureEqual(list(1, 2, 3, 4, 5).rightOf(0), list(2, 3, 4, 5));
    }

    @Test
    public void testSet()
    {
        var list = list(1, 2, 3, 4, 5);
        list.set(0, 9);
        ensureEqual(list, list(9, 2, 3, 4, 5));
        list.set(2, 9);
        ensureEqual(list, list(9, 2, 9, 4, 5));
        list.set(4, 9);
        ensureEqual(list, list(9, 2, 9, 4, 9));
    }

    @Test
    public void testSize()
    {
        ensureEqual(list(1, 2, 3, 4, 5).size(), 5);
        ensureEqual(list().size(), 0);
    }

    @Test
    public void testSorted()
    {
        ensureEqual(list(5, 4, 2, 1, 3).sorted(), list(1, 2, 3, 4, 5));
        ensureEqual(list(5, 4, 2, 1, 3).sorted(Comparator.reverseOrder()), list(5, 4, 3, 2, 1));
    }

    @Test
    public void testStartsWith()
    {
        var list = createList(0, 4);
        var validStart = createList(0, 2);
        var invalidStart1 = createList(1, 4);
        var invalidStart2 = createList(0, 5);

        Assert.assertTrue(list.startsWith(list));
        Assert.assertTrue(list.startsWith(validStart));
        Assert.assertFalse(list.startsWith(invalidStart1));
        Assert.assertFalse(list.startsWith(invalidStart2));
        Assert.assertFalse(list.startsWith(null));
    }

    @Test
    public void testTail()
    {
        ensureEqual(list(1, 2, 3, 4, 5).tail(), list(2, 3, 4, 5));
    }

    @Test
    public void testUnique()
    {
        ensureEqual(list(1, 2, 3), list(1, 2, 3, 1, 2, 3).uniqued());
    }

    @Test
    public void testWith()
    {
        var list = createList(0, 4);
        ensureEqual(list.with(5), createList(0, 5));
    }

    @Test
    public void testWithout()
    {
        ensureEqual(createList(0, 4).without(value -> value == 4), createList(0, 3));
        ensureEqual(list(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).without(value -> value % 2 == 0), list(1, 3, 5, 7, 9));
    }

    private ObjectList<Integer> createList(int firstValue, int lastValue)
    {
        var list = new ObjectList<Integer>();
        for (var i = firstValue; i <= lastValue; i++)
        {
            list.add(i);
        }
        return list;
    }
}
