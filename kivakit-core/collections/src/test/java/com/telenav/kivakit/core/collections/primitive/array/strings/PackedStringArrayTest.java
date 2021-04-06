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
