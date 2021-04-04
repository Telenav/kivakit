////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.path;

import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

public class PackagePathTest
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
        ensure(PackagePath.isPackagePath("a.b.c"));
        ensure(PackagePath.isPackagePath("a"));
        ensure(!PackagePath.isPackagePath("3"));
        ensure(!PackagePath.isPackagePath("3.c"));
        ensure(PackagePath.isPackagePath("a1.b2.c3"));
    }

    @Test
    public void testIterable()
    {
        final var elements = new String[] { "a", "b", "c" };
        int i = 0;
        for (final var element : path("a.b.c"))
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
        final var path = PackagePath.packagePath(PackagePathTest.class);
        final var parent = path.parent();
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
        final var path = PackagePath.packagePath(PackagePathTest.class);
        ensureFalse(path.isRoot());
    }

    @Test
    public void testSeparator()
    {
        final var path = PackagePath.packagePath(PackagePathTest.class);
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
    private PackagePath path(final String path)
    {
        return PackagePath.parsePackagePath(path);
    }
}
