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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.comparison.Matchable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.codecs.GzipCodec;
import com.telenav.kivakit.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.resource.compression.codecs.ZipCodec;
import com.telenav.kivakit.resource.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link FileName} extension, such as ".txt" or ".jar".
 *
 * <p>
 * Common extensions are provided as static constants. An extension can also be constructed with {@link
 * #parseExtension(Listener listener, String)}, with or without a dot prefix:
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
 *     <li>{@link #matcher()} - A matcher that matches resources with this extension</li>
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
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@LexakaiJavadoc(complete = true)
public class Extension implements
        Named,
        Matchable<Resource>
{
    public static final Extension CLASS = parseExtension(".class");

    public static final Extension CSV = parseExtension(".csv");

    public static final Extension GEOJSON = parseExtension(".geojson");

    public static final Extension GRAPH = parseExtension(".graph");

    public static final Extension GRAPH_GZIP = GRAPH.gzipped();

    public static final Extension GZIP = parseExtension(".gz");

    public static final Extension JAR = parseExtension(".jar");

    public static final Extension JAVA = parseExtension(".java");

    public static final Extension JSON = parseExtension(".json");

    public static final Extension KRYO = parseExtension(".kryo");

    public static final Extension MARKDOWN = parseExtension(".md");

    public static final Extension MD5 = parseExtension(".md5");

    public static final Extension OSM = parseExtension(".osm");

    public static final Extension OSMPP = parseExtension(".osmpp");

    public static final Extension OSM_PBF = parseExtension(".osm.pbf");

    public static final Extension PBF = parseExtension(".pbf");

    public static final Extension PNG = parseExtension(".png");

    public static final Extension POLY = parseExtension(".poly");

    public static final Extension POM = parseExtension(".pom");

    public static final Extension PROPERTIES = parseExtension(".properties");

    public static final Extension PYTHON = parseExtension(".py");

    public static final Extension SHA1 = parseExtension(".sha1");

    public static final Extension SHELL = parseExtension(".sh");

    public static final Extension TMP = parseExtension(".tmp");

    public static final Extension TXD = parseExtension(".txd");

    public static final Extension TXD_GZIP = TXD.gzipped();

    public static final Extension TXT = parseExtension(".txt");

    public static final Extension TXT_GZIP = TXT.gzipped();

    public static final Extension XML = parseExtension(".xml");

    public static final Extension YAML = parseExtension(".yaml");

    public static final Extension YML = parseExtension(".yml");

    public static final Extension ZIP = parseExtension(".zip");

    public static List<Extension> archive()
    {
        List<Extension> executable = new ArrayList<>();
        executable.add(JAR);
        executable.add(ZIP);
        executable.add(GZIP);
        return executable;
    }

    public static List<Extension> executable()
    {
        List<Extension> executable = new ArrayList<>();
        executable.add(PYTHON);
        executable.add(SHELL);
        return executable;
    }

    @SuppressWarnings("DuplicatedCode")
    public static List<Extension> known()
    {
        List<Extension> known = new ArrayList<>();
        known.add(CSV);
        known.add(GEOJSON);
        known.add(GRAPH);
        known.add(GRAPH_GZIP);
        known.add(GZIP);
        known.add(JAR);
        known.add(OSM);
        known.add(OSMPP);
        known.add(OSM_PBF);
        known.add(PBF);
        known.add(PNG);
        known.add(POLY);
        known.add(PROPERTIES);
        known.add(SHELL);
        known.add(TXD);
        known.add(TXD_GZIP);
        known.add(TXT);
        known.add(TXT_GZIP);
        known.add(XML);
        known.add(YAML);
        known.add(ZIP);
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

    public static Extension parseExtension(Listener listener, String value)
    {
        if (value.matches("(\\.[A-Za-z0-9]+)+"))
        {
            return new Extension(value);
        }
        throw listener.problem("Cannot parse $", value).asException();
    }

    public static Extension parseExtension(String value)
    {
        return parseExtension(Listener.throwing(), value);
    }

    private final String extension;

    protected Extension(String value)
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
    public boolean ends(Resource resource)
    {
        return resource.hasExtension(this);
    }

    /**
     * True if the given path has this extension
     */
    public boolean ends(ResourcePath path)
    {
        return path.extension().equals(this);
    }

    /**
     * @return True if this extension ends with the given extension. For example the extension ".tar.gz" ends with the
     * extension ".gz"
     */
    public boolean endsWith(Extension extension)
    {
        return this.extension.endsWith(extension.extension);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Extension)
        {
            var that = (Extension) object;
            return extension.equals(that.extension);
        }
        return false;
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

    public Count length()
    {
        return Count.count(toString().length());
    }

    @Override
    @NotNull
    public Matcher<Resource> matcher()
    {
        return resource ->
        {
            var extension = resource.compoundExtension();
            return extension != null && extension.endsWith(this);
        };
    }

    @NotNull
    public Matcher<File> fileMatcher()
    {
        return resource ->
        {
            var extension = resource.compoundExtension();
            return extension != null && extension.endsWith(this);
        };
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

    public Extension withExtension(Extension extension)
    {
        return new Extension(this.extension + extension);
    }
}
