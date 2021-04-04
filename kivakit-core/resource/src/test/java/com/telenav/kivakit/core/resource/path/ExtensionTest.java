////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.path;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class ExtensionTest extends UnitTest
{
    @Test
    public void testKnown()
    {
        Extension previous = null;
        for (final Extension extension : Extension.known())
        {
            if (previous != null)
            {
                ensure(previous.length().isGreaterThanOrEqualTo(extension.length()));
            }
            previous = extension;
        }
    }

    @Test
    public void testWithoutKnownExtension()
    {
        ensureEqual(new FileName("a"), new FileName("a.txt").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.txt.gz").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.osm.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.osm").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a.unknown"), new FileName("a.unknown").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.txt").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.txt.gz").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.osm.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.osm").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c.unknown"), new FileName("a.b.c.unknown").withoutKnownExtensions());
    }
}
