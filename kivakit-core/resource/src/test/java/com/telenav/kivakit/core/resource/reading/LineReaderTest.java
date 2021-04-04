////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.reading;

import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class LineReaderTest extends UnitTest
{
    @Test
    public void test()
    {
        var i = 1;
        final var resource = PackageResource.packageResource(PackagePath.packagePath(getClass()), "test.txt");
        for (final String line : resource.reader().linesAsStringList())
        {
            ensureEqual(i++, Integer.parseInt(line));
        }
    }
}
