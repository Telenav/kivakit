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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

import java.net.URISyntaxException;

public class FileTest extends UnitTest
{
    @Test
    public void testFactory()
    {
        final var temp = File.temporary(Extension.TXT);
        temp.writer().save("hello");
        ensureEqual("hello", File.parse(temp.toString()).reader().string());
        temp.delete();
    }

    @Test
    public void testFileFromURI() throws URISyntaxException
    {
        final Resource resource = File.of(FileTest.class.getResource("FileTest.class").toURI());
        ensureEqual("FileTest.class", resource.fileName().toString());
    }

    @Test
    public void testWithVariables()
    {
        final var test = File.parse("foo/bar/${garply}/banana.txt");
        final var variables = new VariableMap<String>();
        variables.add("garply", "baz");
        ensureEqual(File.parse("foo/bar/baz/banana.txt"), test.expanded(variables));
    }

    @Test
    public void testWithoutCompoundExtension()
    {
        ensureEqual(Folder.parse(".").file("World"), File.parse("./World.osm.pbf").withoutCompoundExtension());
        ensureEqual("World", File.parse("World.osm.pbf").withoutCompoundExtension().toString());
        ensureEqual("World", File.parse("World").withoutCompoundExtension().toString());
        ensureEqual("World", File.parse("World.txt").withoutCompoundExtension().toString());
        ensureEqual(File.parse("/home/users/jonathanl/World"),
                File.parse("/home/users/jonathanl/World.txt").withoutCompoundExtension());
        ensureEqual(File.parse("/home/users/jonathanl/World"),
                File.parse("/home/users/jonathanl/World.osm.pbf").withoutCompoundExtension());
    }

    @Test
    public void testWithoutKnownExtension()
    {
        ensureEqual(File.parse("a"), File.parse("a.txt").withoutKnownExtensions());
        ensureEqual(File.parse("a"), File.parse("a.txt.gz").withoutKnownExtensions());
        ensureEqual(File.parse("a"), File.parse("a.osm.pbf").withoutKnownExtensions());
        ensureEqual(File.parse("a"), File.parse("a.osm").withoutKnownExtensions());
        ensureEqual(File.parse("a"), File.parse("a.pbf").withoutKnownExtensions());
        ensureEqual(File.parse("a.unknown"), File.parse("a.unknown").withoutKnownExtensions());
        ensureEqual(File.parse("a.b.c"), File.parse("a.b.c.txt").withoutKnownExtensions());
        ensureEqual(File.parse("a.b.c"), File.parse("a.b.c.txt.gz").withoutKnownExtensions());
        ensureEqual(File.parse("a.b.c"), File.parse("a.b.c.osm.pbf").withoutKnownExtensions());
        ensureEqual(File.parse("a.b.c"), File.parse("a.b.c.osm").withoutKnownExtensions());
        ensureEqual(File.parse("a.b.c"), File.parse("a.b.c.pbf").withoutKnownExtensions());
        ensureEqual(File.parse("a.b.c.unknown"), File.parse("a.b.c.unknown").withoutKnownExtensions());
    }
}
