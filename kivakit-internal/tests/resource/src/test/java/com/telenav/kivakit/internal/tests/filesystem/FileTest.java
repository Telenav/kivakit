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

package com.telenav.kivakit.internal.tests.filesystem;

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import java.net.URISyntaxException;

import static com.telenav.kivakit.filesystem.File.parseFile;
import static com.telenav.kivakit.filesystem.File.temporaryFile;
import static com.telenav.kivakit.resource.Extension.TXT;
import static java.util.Objects.requireNonNull;

public class FileTest extends UnitTest
{
    @Test
    public void testFactory()
    {
        var temp = temporaryFile(TXT);
        temp.writer().saveText("hello");
        ensureEqual("hello", parseFile(this, temp.toString()).reader().asString());
        temp.delete();
    }

    @Test
    public void testFileFromURI() throws URISyntaxException
    {
        Resource resource = File.file(this, requireNonNull(FileTest.class.getResource("FileTest.class")).toURI());
        ensureEqual("FileTest.class", resource.fileName().toString());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testWithVariables()
    {
        var variables = new VariableMap<String>();
        variables.add("garply", "baz");
        var test = parseFile(this, "foo/bar/${garply}/banana.txt", variables);
        ensureEqual(parseFile(this, "foo/bar/baz/banana.txt"), test);
    }

    @Test
    public void testWithoutCompoundExtension()
    {
        ensureEqual(requireNonNull(Folder.parseFolder(this, ".")).file("World"), parseFile(this, "./World.osm.pbf").withoutCompoundExtension());
        ensureEqual("World", parseFile(this, "World.osm.pbf").withoutCompoundExtension().toString());
        ensureEqual("World", parseFile(this, "World").withoutCompoundExtension().toString());
        ensureEqual("World", parseFile(this, "World.txt").withoutCompoundExtension().toString());
        ensureEqual(parseFile(this, "/home/users/jonathanl/World"),
                parseFile(this, "/home/users/jonathanl/World.txt").withoutCompoundExtension());
        ensureEqual(parseFile(this, "/home/users/jonathanl/World"),
                parseFile(this, "/home/users/jonathanl/World.osm.pbf").withoutCompoundExtension());
    }

    @Test
    public void testWithoutKnownExtension()
    {
        ensureEqual(parseFile(this, "a"), parseFile(this, "a.txt").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a"), parseFile(this, "a.txt.gz").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a"), parseFile(this, "a.osm.pbf").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a"), parseFile(this, "a.osm").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a"), parseFile(this, "a.pbf").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a.unknown"), parseFile(this, "a.unknown").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a.b.c"), parseFile(this, "a.b.c.txt").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a.b.c"), parseFile(this, "a.b.c.txt.gz").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a.b.c"), parseFile(this, "a.b.c.osm.pbf").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a.b.c"), parseFile(this, "a.b.c.osm").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a.b.c"), parseFile(this, "a.b.c.pbf").withoutAllKnownExtensions());
        ensureEqual(parseFile(this, "a.b.c.unknown"), parseFile(this, "a.b.c.unknown").withoutAllKnownExtensions());
    }
}
