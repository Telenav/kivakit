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

package com.telenav.kivakit.internal.tests.core.string;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.logging.loggers.NullLogger;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.util.Arrays;

import static com.telenav.kivakit.core.logging.LoggerFactory.globalLogger;

@SuppressWarnings({ "ConstantConditions", "SameParameterValue" })
public class StringListTest extends CoreUnitTest
{
    @Test
    public void testAdd()
    {
        var list = new StringList();
        ensureEqual(0, list.size());
        ensure(list.isEmpty());
        list.add("a");
        ensureEqual(1, list.size());
        ensureFalse(list.isEmpty());
    }

    @Test
    public void testClear()
    {
        var list = list(5);
        list.clear();
        ensure(list.isEmpty());
        ensureEqual(0, list.size());
    }

    @Test
    public void testJoin()
    {
        var list = list("a", "b", "c");
        ensureEqual("a, b, c", list.join());
        ensureEqual("a::b::c", list.join("::"));
    }

    @Test
    public void testMaximum()
    {
        globalLogger(new NullLogger());
        StringList list = new StringList(Maximum._1);
        list.add("a");
        list.add("b");
        ensureEqual(1, list.size());
    }

    @Test
    public void testNumbered()
    {
        var list = list("a", "b", "c");
        var numbered = list.numbered();
        ensureEqual("1. a, 2. b, 3. c", numbered.join());
    }

    @Test
    public void testReverse()
    {
        var list = list("1", "2", "3");
        list.reverse();
        ensureEqual("3", list.get(0));
        ensureEqual("2", list.get(1));
        ensureEqual("1", list.get(2));
    }

    @Test
    public void testSplit()
    {
        var list = StringList.split("a,b,c", ",");
        ensureEqual("a, b, c", list.join());
    }

    @Test
    public void testWordSplit()
    {
        ensureEqual(StringList.stringList("a", "b", "c"), StringList.words("a b c"));
        ensureEqual(StringList.stringList("a", "b", "c"), StringList.words("a b c "));
        ensureEqual(StringList.stringList("a", "b", "c"), StringList.words(" a b c"));
        ensureEqual(StringList.stringList("a", "b", "c"), StringList.words(" a  b    c"));
    }

    private StringList list(int elements)
    {
        var list = new StringList();
        for (var i = 0; i < elements; i++)
        {
            list.add("element" + i);
        }
        return list;
    }

    private StringList list(String... elements)
    {
        var list = new StringList();
        list.addAll(Arrays.asList(elements));
        return list;
    }
}
