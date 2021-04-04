////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.array.strings;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;
import org.junit.Test;

public class PackedStringArrayTest extends CoreCollectionsUnitTest
{
    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testMixed()
    {
        final var a = new PackedStringArray("test");
        a.initialize();
        ensure(a.size() == 0);
        final var a1 = a.add("test");
        final var a2 = a.add("test\u1234");
        final var a3 = a.add("test\u0085");
        final var a4 = a.add("foobar");
        a.compress(CompressibleCollection.Method.RESIZE);
        ensureEqual("test", a.get(a1));
        ensureEqual("foobar", a.get(a4));
        ensureEqual("test\u0085", a.get(a3));
        ensureEqual("test\u1234", a.get(a2));
    }

    @Test
    public void testMultiple()
    {
        final var a = new PackedStringArray("test");
        a.initialize();
        ensure(a.size() == 0);
        final var a1 = a.add("test");
        final var a2 = a.add("test");
        final var a3 = a.add("test");
        final var a4 = a.add("foobar");
        a.compress(CompressibleCollection.Method.RESIZE);
        ensureEqual("test", a.get(a1));
        ensureEqual("foobar", a.get(a4));
        ensureEqual("test", a.get(a2));
        ensureEqual("test", a.get(a3));
    }

    @Test
    public void testSimple()
    {
        final var a = new PackedStringArray("test");
        a.initialize();
        ensure(a.size() == 0);
        final var a1 = a.add("test");
        ensure(a.size() == 1);
        a.compress(CompressibleCollection.Method.RESIZE);
        ensureEqual("test", a.get(a1));
    }
}
