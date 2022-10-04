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

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.resource.FileName.parseFileName;

public class ExtensionTest extends UnitTest
{
    @Test
    public void testWithoutKnownExtension()
    {
        ensureEqual(parseFileName(this, "a"), parseFileName(this, "a.txt").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a"), parseFileName(this, "a.txt.gz").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a"), parseFileName(this, "a.osm.pbf").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a"), parseFileName(this, "a.osm").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a"), parseFileName(this, "a.pbf").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a.unknown"), parseFileName(this, "a.unknown").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a.b.c"), parseFileName(this, "a.b.c.txt").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a.b.c"), parseFileName(this, "a.b.c.txt.gz").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a.b.c"), parseFileName(this, "a.b.c.osm.pbf").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a.b.c"), parseFileName(this, "a.b.c.osm").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a.b.c"), parseFileName(this, "a.b.c.pbf").withoutKnownExtensions());
        ensureEqual(parseFileName(this, "a.b.c.unknown"), parseFileName(this, "a.b.c.unknown").withoutKnownExtensions());
    }
}
