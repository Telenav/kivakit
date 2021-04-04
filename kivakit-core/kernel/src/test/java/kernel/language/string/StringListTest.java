////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.string;

import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import org.junit.Test;

import java.util.Arrays;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

@SuppressWarnings("ConstantConditions")
public class StringListTest
{
    @Test
    public void testAdd()
    {
        final var list = new StringList();
        ensureEqual(0, list.size());
        ensure(list.isEmpty());
        list.add("a");
        ensureEqual(1, list.size());
        ensureFalse(list.isEmpty());
    }

    @Test
    public void testClear()
    {
        final var list = list(5);
        list.clear();
        ensure(list.isEmpty());
        ensureEqual(0, list.size());
    }

    @Test
    public void testJoin()
    {
        final var list = list("a", "b", "c");
        ensureEqual("a, b, c", list.join());
        ensureEqual("a::b::c", list.join("::"));
    }

    @Test
    public void testMaximum()
    {
        final StringList list = new StringList(Maximum._1)
        {
            @Override
            protected void onOutOfRoom()
            {
            }
        };
        list.add("a");
        list.add("b");
        ensureEqual(1, list.size());
    }

    @Test
    public void testNumbered()
    {
        final var list = list("a", "b", "c");
        final var numbered = list.numbered();
        ensureEqual("1. a, 2. b, 3. c", numbered.join());
    }

    @Test
    public void testReverse()
    {
        final var list = list("1", "2", "3");
        list.reverse();
        ensureEqual("3", list.get(0));
        ensureEqual("2", list.get(1));
        ensureEqual("1", list.get(2));
    }

    @Test
    public void testSplit()
    {
        final var list = StringList.split("a,b,c", ",");
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

    private StringList list(final int elements)
    {
        final var list = new StringList();
        for (var i = 0; i < elements; i++)
        {
            list.add("element" + i);
        }
        return list;
    }

    private StringList list(final String... elements)
    {
        final var list = new StringList();
        list.addAll(Arrays.asList(elements));
        return list;
    }
}
