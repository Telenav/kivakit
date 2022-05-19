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

package com.telenav.kivakit.internal.tests.resource;

import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.test.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;

public class ResourcePathTest extends UnitTest
{
    @Test
    public void testAppend()
    {
        ensureEqual(path("a/b/c").withChild("d"), path("a/b/c/d"));
    }

    @Test
    public void testChild()
    {
        ensureEqual(path("a/b/c").withChild("d"), path("a/b/c/d"));
    }

    @Test
    public void testFirst()
    {
        ensureEqual(path("a/b/c").first(), "a");
        ensureEqual(path("a/b/c").first(2), path("a/b"));
    }

    @Test
    public void testGet()
    {
        ensureEqual(path("a/b/c").get(0), "a");
        ensureEqual(path("a/b/c").get(1), "b");
        ensureEqual(path("a/b/c").get(2), "c");
    }

    @Test
    public void testIterable()
    {
        var elements = new String[] { "a", "b", "c" };
        int i = 0;
        for (var element : path("a/b/c"))
        {
            ensureEqual(element, elements[i++]);
        }
    }

    @Test
    public void testLast()
    {
        ensureEqual(path("a/b/c").last(), "c");
    }

    @Test
    public void testNormalize()
    {
        if (isWindows())
        {
            ensureEqual(path("a\\b\\c").normalized(), path("a\\b\\c"));
        }
        else
        {
            ensureEqual(path("a/b/c").normalized(), path("a/b/c"));
        }
        ensureEqual(path("hdfs://a/b/c").normalized(), path("hdfs://a/b/c"));
    }

    @Test
    public void testParent()
    {
        var path = path("a/b/c");
        var parent = path.parent();
        ensure(path.startsWith(parent));
        ensureEqual(parent, path("a/b"));
    }

    @Test
    public void testPrepend()
    {
        ensureEqual(path("a/b/c").withParent("z"), path("z/a/b/c"));
    }

    @Test
    public void testRoot()
    {
        if (isWindows())
        {
            var path = ResourcePath.parseResourcePath(this, "c:\\");
            ensure(path.isRoot());
        }
        else
        {
            var path = ResourcePath.parseResourcePath(this, "/");
            ensure(path.isRoot());
        }
    }

    @Test
    public void testSeparator()
    {
        ensureEqual(path("a").separator(), File.separator);
    }

    @Test
    public void testSize()
    {
        ensureEqual(path("a/b/c").size(), 3);
    }

    @Test
    public void testToString()
    {
        if (isWindows())
        {
            ensureEqual(path("a/b/c").toString(), "a\\b\\c");
        }
        else
        {
            ensureEqual(path("a/b/c").toString(), "a/b/c");
        }
    }

    @Test
    public void testWithoutFirst()
    {
        ensureEqual(path("a/b/c").withoutFirst(), path("b/c"));
    }

    @Test
    public void testWithoutLast()
    {
        ensureEqual(path("a/b/c").withoutLast(), path("a/b"));
    }

    @Test
    public void testWithoutPrefix()
    {
        ensureEqual(path("a/b/c").withoutPrefix(path("a/b")), path("c"));
    }

    @Test
    public void testWithoutSuffix()
    {
        ensureEqual(path("a/b/c").withoutSuffix(path("b/c")), path("a"));
    }

    @NotNull
    private ResourcePath path(String path)
    {
        return ResourcePath.parseResourcePath(this, path);
    }
}
