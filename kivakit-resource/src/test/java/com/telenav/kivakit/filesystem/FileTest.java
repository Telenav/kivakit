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

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.path.Extension;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Objects;

public class FileTest extends UnitTest
{
    @Test
    public void testFactory()
    {
        var temp = File.temporary(Extension.TXT);
        temp.writer().save("hello");
        ensureEqual("hello", File.parse(this, temp.toString()).reader().asString());
        temp.delete();
    }

    @Test
    public void testFileFromURI() throws URISyntaxException
    {
        Resource resource = File.file(this, Objects.requireNonNull(FileTest.class.getResource("FileTest.class")).toURI());
        ensureEqual("FileTest.class", resource.fileName().toString());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testWithVariables()
    {
        var variables = new VariableMap<String>();
        variables.add("garply", "baz");
        var test = File.parse(this, "foo/bar/${garply}/banana.txt", variables);
        ensureEqual(File.parse(this, "foo/bar/baz/banana.txt"), test);
    }

    @Test
    public void testWithoutCompoundExtension()
    {
        ensureEqual(Objects.requireNonNull(Folder.parse(this, ".")).file("World"), File.parse(this, "./World.osm.pbf").withoutCompoundExtension());
        ensureEqual("World", File.parse(this, "World.osm.pbf").withoutCompoundExtension().toString());
        ensureEqual("World", File.parse(this, "World").withoutCompoundExtension().toString());
        ensureEqual("World", File.parse(this, "World.txt").withoutCompoundExtension().toString());
        ensureEqual(File.parse(this, "/home/users/jonathanl/World"),
                File.parse(this, "/home/users/jonathanl/World.txt").withoutCompoundExtension());
        ensureEqual(File.parse(this, "/home/users/jonathanl/World"),
                File.parse(this, "/home/users/jonathanl/World.osm.pbf").withoutCompoundExtension());
    }

    @Test
    public void testWithoutKnownExtension()
    {
        ensureEqual(File.parse(this, "a"), File.parse(this, "a.txt").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a"), File.parse(this, "a.txt.gz").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a"), File.parse(this, "a.osm.pbf").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a"), File.parse(this, "a.osm").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a"), File.parse(this, "a.pbf").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a.unknown"), File.parse(this, "a.unknown").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a.b.c"), File.parse(this, "a.b.c.txt").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a.b.c"), File.parse(this, "a.b.c.txt.gz").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a.b.c"), File.parse(this, "a.b.c.osm.pbf").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a.b.c"), File.parse(this, "a.b.c.osm").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a.b.c"), File.parse(this, "a.b.c.pbf").withoutKnownExtensions());
        ensureEqual(File.parse(this, "a.b.c.unknown"), File.parse(this, "a.b.c.unknown").withoutKnownExtensions());
    }
}
