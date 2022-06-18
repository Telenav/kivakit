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

package com.telenav.kivakit.internal.tests.core.path;

import com.telenav.kivakit.core.language.module.PackageReference;import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PackagePathTest extends CoreUnitTest
{
    @Test
    public void testAppend()
    {
        ensureEqual(path("a.b.c").withChild("d"), path("a.b.c.d"));
    }

    @Test
    public void testChild()
    {
        ensureEqual(path("a.b.c").withChild("d"), path("a.b.c.d"));
    }

    @Test
    public void testFirst()
    {
        ensureEqual(path("a.b.c").first(), "a");
        ensureEqual(path("a.b.c").first(2), path("a.b"));
    }

    @Test
    public void testGet()
    {
        ensureEqual(path("a.b.c").get(0), "a");
        ensureEqual(path("a.b.c").get(1), "b");
        ensureEqual(path("a.b.c").get(2), "c");
    }

    @Test
    public void testIs()
    {
        ensure(PackageReference.isPackageReference("a.b.c"));
        ensure(PackageReference.isPackageReference("a"));
        ensure(!PackageReference.isPackageReference("3"));
        ensure(!PackageReference.isPackageReference("3.c"));
        ensure(PackageReference.isPackageReference("a1.b2.c3"));
    }

    @Test
    public void testIterable()
    {
        var elements = new String[] { "a", "b", "c" };
        int i = 0;
        for (var element : path("a.b.c"))
        {
            ensureEqual(element, elements[i++]);
        }
    }

    @Test
    public void testLast()
    {
        ensureEqual(path("a.b.c").last(), "c");
    }

    @Test
    public void testParent()
    {
        var path = PackageReference.packageReference(PackagePathTest.class);
        var parent = path.parent();
        ensure(path.startsWith(parent));
    }

    @Test
    public void testPrepend()
    {
        ensureEqual(path("a.b.c").withParent("z"), path("z.a.b.c"));
    }

    @Test
    public void testRoot()
    {
        var path = PackageReference.packageReference(PackagePathTest.class);
        ensureFalse(path.isRoot());
    }

    @Test
    public void testSeparator()
    {
        var path = PackageReference.packageReference(PackagePathTest.class);
        ensureEqual(path.separator(), ".");
    }

    @Test
    public void testSize()
    {
        ensureEqual(path("a.b.c").size(), 3);
    }

    @Test
    public void testToString()
    {
        ensureEqual(path("a.b.c").toString(), "a.b.c");
    }

    @Test
    public void testWithoutFirst()
    {
        ensureEqual(path("a.b.c").withoutFirst(), path("b.c"));
    }

    @Test
    public void testWithoutLast()
    {
        ensureEqual(path("a.b.c").withoutLast(), path("a.b"));
    }

    @Test
    public void testWithoutPrefix()
    {
        ensureEqual(path("a.b.c").withoutPrefix(path("a.b")), path("c"));
    }

    @Test
    public void testWithoutSuffix()
    {
        ensureEqual(path("a.b.c").withoutSuffix(path("b.c")), path("a"));
    }

    @NotNull
    private PackageReference path(String path)
    {
        return PackageReference.parsePackageReference(this, path);
    }
}
