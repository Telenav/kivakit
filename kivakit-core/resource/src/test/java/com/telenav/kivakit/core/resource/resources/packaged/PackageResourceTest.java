////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.packaged;

import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * @author jonathanl (shibo)
 */
public class PackageResourceTest
{
    @Test
    public void testLastModified()
    {
        ensure(a().lastModified().isAfter(Time.START_OF_UNIX_TIME));
    }

    @Test
    public void testPath()
    {
        final var fullPath = a().path();
        ensureEqual(fullPath.join("/"), "com/telenav/kivakit/core/resource/resources/packaged/a.txt");
    }

    @Test
    public void testRead()
    {
        ensureEqual(a().reader().string().trim(), "123");
        ensureEqual(b().reader().string().trim(), "1234");
    }

    @Test
    public void testSize()
    {
        ensure(a().bytes().isGreaterThanOrEqualTo(Bytes.bytes(4)));
        ensure(b().bytes().isGreaterThanOrEqualTo(Bytes.bytes(5)));
    }

    @NotNull
    private PackagePath _package()
    {
        return PackagePath.packagePath(getClass());
    }

    private PackageResource a()
    {
        return PackageResource.packageResource(_package(), "a.txt");
    }

    private PackageResource b()
    {
        return PackageResource.packageResource(_package(), "b.txt");
    }
}
