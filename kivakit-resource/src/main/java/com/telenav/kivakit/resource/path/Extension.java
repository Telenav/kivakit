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

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.codecs.GzipCodec;
import com.telenav.kivakit.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.resource.compression.codecs.ZipCodec;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * A {@link FileName} extension, such as ".txt" or ".jar".
 *
 * <p>
 * Common extensions are provided as static constants. An extension can also be constructed with {@link #parse(String)},
 * with or without a dot prefix:
 * </p>
 *
 * <pre>
 * Extension.parse(".xyz");
 * Extension.parse("xyz");
 * </pre>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()} - The extension as a string</li>
 *     <li>{@link #codec()} - The codec normally used for files with this extension, or the {@link NullCodec} if the file is not compressed</li>
 * </ul>
 *
 * <p><b>Matching</b></p>
 *
 * <ul>
 *     <li>{@link #fileMatcher()} - A matcher that matches resources with this extension</li>
 *     <li>{@link #ends(Resource)} - True if the given resource has this extension</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #endsWith(Extension)} - True if this (compound) extension ends with the given extension</li>
 *     <li>{@link #isArchive()} - True if this extensions indicates an archive</li>
 *     <li>{@link #isExecutable()} - True if this extension is normally executable</li>
 * </ul>
 *
 * <p><b>Functional Methods</b></p>
 *
 * <ul>
 *     <li>{@link #gzipped()} - This extension with ".gz" on the end</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@LexakaiJavadoc(complete = true)
public class Extension implements Named
{
    // Archives
    public static final Extension GZIP = parse(".gz");

    public static final Extension ZIP = parse(".zip");

    public static final Extension JAR = parse(".jar");

    public static final Extension KRYO = parse(".kryo");

    // Executable files
    public static final Extension PYTHON = parse(".py");

    public static final Extension SHELL = parse(".sh");

    // General file formats
    public static final Extension PROPERTIES = parse(".properties");

    public static final Extension TXT = parse(".txt");

    public static final Extension CSV = parse(".csv");

    public static final Extension PNG = parse(".png");

    public static final Extension TMP = parse(".tmp");

    // Map file formats
    public static final Extension TXD = parse(".txd");

    public static final Extension GRAPH = parse(".graph");

    public static final Extension PBF = parse(".pbf");

    public static final Extension OSM = parse(".osm");

    public static final Extension OSM_PBF = parse(".osm.pbf");

    public static final Extension POLY = parse(".poly");

    public static final Extension GEOJSON = parse(".geojson");

    public static final Extension OSMPP = parse(".osmpp");

    // Compressed formats
    public static final Extension TXT_GZIP = TXT.gzipped();

    public static final Extension TXD_GZIP = TXD.gzipped();

    public static final Extension GRAPH_GZIP = GRAPH.gzipped();

    public static final Extension JAVA = parse(".java");

    public static List<Extension> archive()
    {
        final List<Extension> executable = new ArrayList<>();
        executable.add(JAR);
        executable.add(ZIP);
        executable.add(GZIP);
        return executable;
    }

    public static List<Extension> executable()
    {
        final List<Extension> executable = new ArrayList<>();
        executable.add(PYTHON);
        executable.add(SHELL);
        return executable;
    }

    public static List<Extension> known()
    {
        final List<Extension> known = new ArrayList<>();
        known.add(GZIP);
        known.add(ZIP);
        known.add(PROPERTIES);
        known.add(TXT);
        known.add(CSV);
        known.add(PNG);
        known.add(JAR);
        known.add(SHELL);
        known.add(TXD);
        known.add(GRAPH);
        known.add(PBF);
        known.add(OSM);
        known.add(OSM_PBF);
        known.add(POLY);
        known.add(GEOJSON);
        known.add(OSMPP);
        known.add(TXT_GZIP);
        known.add(TXD_GZIP);
        known.add(GRAPH_GZIP);
        known.sort((a, b) ->
        {
            if (a.length().isLessThan(b.length()))
            {
                return 1;
            }
            if (a.length().isGreaterThan(b.length()))
            {
                return -1;
            }
            return 0;
        });
        return known;
    }

    public static Extension parse(final String value)
    {
        if (value.matches("(\\.[A-Za-z]+)+"))
        {
            return new Extension(value);
        }
        fail("Cannot parse $", value);
        throw new IllegalStateException();
    }

    private final String extension;

    protected Extension(final String value)
    {
        if (value.startsWith("."))
        {
            extension = value.substring(1);
        }
        else
        {
            extension = value;
        }
    }

    /**
     * @return The codec normally used for files with this extension, or the {@link NullCodec} if the file is not
     * compressed
     */
    public Codec codec()
    {
        if (endsWith(GZIP))
        {
            return new GzipCodec();
        }
        if (endsWith(ZIP))
        {
            return new ZipCodec();
        }
        return new NullCodec();
    }

    /**
     * True if the given resource has this extension
     */
    public boolean ends(final Resource resource)
    {
        return resource.hasExtension(this);
    }

    /**
     * True if the given path has this extension
     */
    public boolean ends(final FilePath path)
    {
        return path.extension().equals(this);
    }

    /**
     * @return True if this extension ends with the given extension. For example the extension ".tar.gz" ends with the
     * extension ".gz"
     */
    public boolean endsWith(final Extension extension)
    {
        return this.extension.endsWith(extension.extension);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Extension)
        {
            final var that = (Extension) object;
            return extension.equals(that.extension);
        }
        return false;
    }

    @NotNull
    public Matcher<File> fileMatcher()
    {
        return file ->
        {
            final var extension = file.compoundExtension();
            return extension != null && extension.endsWith(this);
        };
    }

    public Extension gzipped()
    {
        return new Extension(extension + ".gz");
    }

    @Override
    public int hashCode()
    {
        return extension.hashCode();
    }

    public boolean isArchive()
    {
        return archive().contains(this);
    }

    public boolean isExecutable()
    {
        return executable().contains(this);
    }

    @Override
    public String name()
    {
        return extension;
    }

    @Override
    public String toString()
    {
        return "." + extension;
    }

    Count length()
    {
        return Count.count(toString().length());
    }
}
