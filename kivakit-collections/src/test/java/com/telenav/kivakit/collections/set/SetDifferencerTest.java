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

package com.telenav.kivakit.collections.set;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;

@SuppressWarnings("SpellCheckingInspection")
public class SetDifferencerTest extends CoreUnitTest
{
    private static class Value
    {
        private int id;

        @Override
        public boolean equals(Object object)
        {
            if (object instanceof SetDifferencerTest.Value)
            {
                var that = (SetDifferencerTest.Value) object;
                return id == that.id;
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return id;
        }

        @Override
        public String toString()
        {
            return id + "(" + name + ")";
        }

        String name;
    }

    @Test
    public void test()
    {
        // random().seed(1900598128L);

        // -> 1234 = add 1, 2, 3, 4
        check(ids(), ids(1, 2, 3, 4), ids(1, 2, 3, 4), ids(), ids());

        // 1234 -> = remove 1, 2, 3, 4
        check(ids(1, 2, 3, 4), ids(), ids(), ids(1, 2, 3, 4), ids());

        // 1234 -> 3456 = add 5, 6 + remove 1, 2
        check(ids(1, 2, 3, 4), ids(3, 4, 5, 6), ids(5, 6), ids(1, 2), ids());

        // 1234 -> 1234 =
        check(ids(1, 2, 3, 4), ids(1, 2, 3, 4), ids(), ids(), ids());

        // 1234 -> 1(one)234 = updated 1(one)
        check(ids(1, 2, 3, 4), values(value(1, "one"), value(2), value(3), value(4)), ids(), ids(),
                values(value(1, "one")));

        // 1234 -> 1(one)23(three)4 = updated 1(one)3(three)
        check(ids(1, 2, 3, 4), values(value(1, "one"), value(2), value(3, "three"), value(4)), ids(), ids(),
                values(value(1, "one"), value(3, "three")));

        // 1234 -> 3(three)456 = add 5, 6 + remove 1, 2 + update 3(three)
        check(ids(1, 2, 3, 4), values(value(3, "three"), value(4), value(5), value(6)), ids(5, 6), ids(1, 2),
                values(value(3, "three")));
    }

    private void check(Set<Value> before, Set<Value> after, Set<Value> expectedAdded,
                       Set<Value> expectedRemoved, Set<Value> expectedUpdated)
    {
        Set<Value> added = new HashSet<>();
        Set<Value> removed = new HashSet<>();
        Set<Value> updated = new HashSet<>();

        new SetDifferencer<Value>((a, b) -> a.equals(b) && a.name.equals(b.name))
        {
            @Override
            protected void onAdded(Value value)
            {
                added.add(value);
                trace("added " + value);
            }

            @Override
            protected void onRemoved(Value value)
            {
                removed.add(value);
                trace("removed " + value);
            }

            @Override
            protected void onUpdated(Value value)
            {
                updated.add(value);
                trace("updated " + value);
            }
        }.compare(before, after);

        ensureEqual(expectedAdded, added);
        ensureEqual(expectedRemoved, removed);
        ensureEqual(expectedUpdated, updated);
    }

    private Set<Value> ids(int... values)
    {
        Set<Value> set = new HashSet<>();
        for (int value : values)
        {
            set.add(value(value));
        }
        return set;
    }

    private Value value(int id)
    {
        return value(id, "" + id);
    }

    private Value value(int id, String name)
    {
        var value = new Value();
        value.id = id;
        value.name = name;
        return value;
    }

    private ObjectSet<Value> values(Value... values)
    {
        return set(values);
    }
}
