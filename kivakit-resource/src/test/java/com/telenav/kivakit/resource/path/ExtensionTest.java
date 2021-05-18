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

package com.telenav.kivakit.resource.path;

import com.telenav.kivakit.test.UnitTest;
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
        ensureEqual(FileName.parse("a"), FileName.parse("a.txt").withoutKnownExtensions());
        ensureEqual(FileName.parse("a"), FileName.parse("a.txt.gz").withoutKnownExtensions());
        ensureEqual(FileName.parse("a"), FileName.parse("a.osm.pbf").withoutKnownExtensions());
        ensureEqual(FileName.parse("a"), FileName.parse("a.osm").withoutKnownExtensions());
        ensureEqual(FileName.parse("a"), FileName.parse("a.pbf").withoutKnownExtensions());
        ensureEqual(FileName.parse("a.unknown"), FileName.parse("a.unknown").withoutKnownExtensions());
        ensureEqual(FileName.parse("a.b.c"), FileName.parse("a.b.c.txt").withoutKnownExtensions());
        ensureEqual(FileName.parse("a.b.c"), FileName.parse("a.b.c.txt.gz").withoutKnownExtensions());
        ensureEqual(FileName.parse("a.b.c"), FileName.parse("a.b.c.osm.pbf").withoutKnownExtensions());
        ensureEqual(FileName.parse("a.b.c"), FileName.parse("a.b.c.osm").withoutKnownExtensions());
        ensureEqual(FileName.parse("a.b.c"), FileName.parse("a.b.c.pbf").withoutKnownExtensions());
        ensureEqual(FileName.parse("a.b.c.unknown"), FileName.parse("a.b.c.unknown").withoutKnownExtensions());
    }
}
