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

package com.telenav.kivakit.internal.tests.resource.path;

import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.testing.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Objects;

import static com.telenav.kivakit.filesystem.FilePath.filePath;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.resource.UriSchemes.uriScheme;

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
        ensureEqual(parseFilePath(this, "TestFile1.txt").asAbsolute(), parseFilePath(this, "TestFile1.txt").asAbsolute());

        var filePath1 = parseFilePath(this, "TestFile1.txt");
        var filePath1a = parseFilePath(this, "TestFile1.txt");
        var filePath2 = parseFilePath(this, "TestFile2.txt");
        var directoryName = "newdirectory";
        var fileName = "TestFile3.txt";

        ensureEqual(filePath1, filePath1a);
        ensure(filePath1.equals(filePath1a));
        ensureFalse(filePath1.equals(filePath2));

        var filePath3 = parseFilePath(this, directoryName).withChild(fileName);
        ensureEqual(filePath3.toString(), directoryName + filePath1.separator() + fileName);

        ensure(parseFilePath(this, fileName).asAbsolute().toString().endsWith(parseFilePath(this, fileName).separator() + fileName));
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
        if (OperatingSystem.operatingSystem().isWindows())
        {
            var rawPath = "C:\\this\\is\\a\\test\\path";
            var path = parseFilePath(this, rawPath);
            ensureEqual(rawPath, path.join());

            var rawPathWithTrailingBackslash = "C:\\this\\is\\a\\test\\path\\";
            path = parseFilePath(this, rawPathWithTrailingBackslash);
            ensureEqual(rawPath, path.join());

            rawPath = "C:";
            path = parseFilePath(this, rawPath);
            ensureEqual(rawPath, path.join());
        }
        else
        {
            var rawPath = "/this/is/a/test/path";
            var path = parseFilePath(this, rawPath);
            ensureEqual(path.join(), rawPath);

            var rawPathWithTrailingBackslash = "/this/is/a/test/path/";
            path = parseFilePath(this, rawPathWithTrailingBackslash);
            ensureEqual(rawPathWithTrailingBackslash, path.join());

            rawPath = "/";
            path = parseFilePath(this, rawPath);
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
    public void testParseZipPath()
    {
        var path = "/Users/jonathan/temporary/processes/kivakit-process-21718/test.jar!/ExampleBuild-project.properties";
        var filePath = filePath(URI.create("jar:file:///Users/jonathan/temporary/processes/kivakit-process-21718/test.jar!/ExampleBuild-project.properties"));
        ensure(filePath.schemes().size() == 2);
        ensureEqual(filePath.schemes().get(0), "jar");
        ensureEqual(filePath.schemes().get(1), "file");
        ensureEqual(filePath.withoutSchemes().path().asString(), path);
    }

    @Test
    public void testPrepend()
    {
        ensureEqual(path("a/b/c").withParent("z"), path("z/a/b/c"));
    }

    @Test
    public void testRoot()
    {
        if (OperatingSystem.operatingSystem().isWindows())
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
        if (OperatingSystem.operatingSystem().isWindows())
        {
            var rawPath = "C:\\this\\is\\a\\test\\path";
            var path = parseFilePath(this, rawPath);
            var root = Objects.requireNonNull(Folder.parseFolder(this, "C:\\")).path().asAbsolute();
            var root2 = path.root().asAbsolute();
            ensureEqual(root, root2);
        }
        else
        {
            var rawPath = "/this/is/a/test/path";
            var path = parseFilePath(this, rawPath);
            ensureEqual("/", path.root().toString());
        }
    }

    @Test
    public void testScheme()
    {
        var path = parseFilePath(this, "hdfs://192.168.0.1/user/jonathanl/test.txt");
        ensureEqual(path.schemes(), uriScheme("hdfs"));
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
        if (OperatingSystem.operatingSystem().isWindows())
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
        return parseFilePath(this, path);
    }
}
