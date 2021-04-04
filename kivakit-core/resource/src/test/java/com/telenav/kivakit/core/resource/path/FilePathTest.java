////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.path;

import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.core.test.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;

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
        final var elements = new String[] { "a", "b", "c" };
        int i = 0;
        for (final var element : path("a/b/c"))
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
            var path = FilePath.parseFilePath(rawPath);
            ensureEqual(rawPath, path.join());

            final var rawPathWithTrailingBackslash = "C:\\this\\is\\a\\test\\path\\";
            path = FilePath.parseFilePath(rawPathWithTrailingBackslash);
            ensureEqual(rawPath, path.join());

            rawPath = "C:";
            path = FilePath.parseFilePath(rawPath);
            ensureEqual(rawPath, path.join());
        }
        else
        {
            var rawPath = "/this/is/a/test/path";
            var path = FilePath.parseFilePath(rawPath);
            ensureEqual(path.join(), rawPath);

            final var rawPathWithTrailingBackslash = "/this/is/a/test/path/";
            path = FilePath.parseFilePath(rawPathWithTrailingBackslash);
            ensureEqual(rawPath, path.join());

            rawPath = "/";
            path = FilePath.parseFilePath(rawPath);
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
        final var path = path("a/b/c");
        final var parent = path.parent();
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
            final var path = ResourcePath.parseResourcePath("c:\\");
            ensure(path.isRoot());
        }
        else
        {
            final var path = ResourcePath.parseResourcePath("/");
            ensure(path.isRoot());
        }
    }

    @Test
    public void testRootFolder()
    {
        if (OperatingSystem.get().isWindows())
        {
            final var rawPath = "C:\\this\\is\\a\\test\\path";
            final var path = FilePath.parseFilePath(rawPath);
            final var root = Folder.parse("C:\\").path().absolute();
            final var root2 = path.root().absolute();
            ensureEqual(root, root2);
        }
        else
        {
            final var rawPath = "/this/is/a/test/path";
            final var path = FilePath.parseFilePath(rawPath);
            ensureEqual("/", path.root().toString());
        }
    }

    @Test
    public void testScheme()
    {
        final var path = FilePath.parseFilePath("hdfs://192.168.0.1/user/jonathanl/test.txt");
        ensureEqual("hdfs", path.scheme());
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
    private FilePath path(final String path)
    {
        return FilePath.parseFilePath(path);
    }
}
