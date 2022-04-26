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

package com.telenav.kivakit.tests.resource.path;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourcePath;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.util.Objects;

public class FilePathTest extends UnitTest
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
    @SuppressWarnings("SpellCheckingInspection")
    public void testFilePath()
    {
        ensureEqual(FilePath.parseFilePath(this, "TestFile1.txt").absolute(), FilePath.parseFilePath(this, "TestFile1.txt").absolute());

        var filePath1 = FilePath.parseFilePath(this, "TestFile1.txt");
        var filePath1a = FilePath.parseFilePath(this, "TestFile1.txt");
        var filePath2 = FilePath.parseFilePath(this, "TestFile2.txt");
        var directoryName = "newdirectory";
        var fileName = "TestFile3.txt";

        ensureEqual(filePath1, filePath1a);
        ensure(filePath1.equals(filePath1a));
        ensureFalse(filePath1.equals(filePath2));

        var filePath3 = FilePath.parseFilePath(this, directoryName).withChild(fileName);
        ensureEqual(filePath3.toString(), directoryName + filePath1.separator() + fileName);

        ensure(FilePath.parseFilePath(this, fileName).absolute().toString().endsWith(FilePath.parseFilePath(this, fileName).separator() + fileName));
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
    public void testJoin()
    {
        if (OperatingSystem.get().isWindows())
        {
            var rawPath = "C:\\this\\is\\a\\test\\path";
            var path = FilePath.parseFilePath(this, rawPath);
            ensureEqual(rawPath, path.join());

            var rawPathWithTrailingBackslash = "C:\\this\\is\\a\\test\\path\\";
            path = FilePath.parseFilePath(this, rawPathWithTrailingBackslash);
            ensureEqual(rawPath, path.join());

            rawPath = "C:";
            path = FilePath.parseFilePath(this, rawPath);
            ensureEqual(rawPath, path.join());
        }
        else
        {
            var rawPath = "/this/is/a/test/path";
            var path = FilePath.parseFilePath(this, rawPath);
            ensureEqual(path.join(), rawPath);

            var rawPathWithTrailingBackslash = "/this/is/a/test/path/";
            path = FilePath.parseFilePath(this, rawPathWithTrailingBackslash);
            ensureEqual(rawPath, path.join());

            rawPath = "/";
            path = FilePath.parseFilePath(this, rawPath);
            ensureEqual(rawPath, path.join());
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
        ensureEqual(path("hdfs://a/b/c").normalized(), path("hdfs://a/b/c"));
        ensureEqual(path("a/b/c").normalized(), path("a/b/c"));
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
        if (OperatingSystem.get().isWindows())
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
    public void testRootFolder()
    {
        if (OperatingSystem.get().isWindows())
        {
            var rawPath = "C:\\this\\is\\a\\test\\path";
            var path = FilePath.parseFilePath(this, rawPath);
            var root = Objects.requireNonNull(Folder.parse(this, "C:\\")).path().absolute();
            var root2 = path.root().absolute();
            ensureEqual(root, root2);
        }
        else
        {
            var rawPath = "/this/is/a/test/path";
            var path = FilePath.parseFilePath(this, rawPath);
            ensureEqual("/", path.root().toString());
        }
    }

    @Test
    public void testScheme()
    {
        var path = FilePath.parseFilePath(this, "hdfs://192.168.0.1/user/jonathanl/test.txt");
        ensureEqual(path.schemes(), StringList.stringList("hdfs"));
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
        if (OperatingSystem.get().isWindows())
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
    private FilePath path(String path)
    {
        return FilePath.parseFilePath(this, path);
    }
}
