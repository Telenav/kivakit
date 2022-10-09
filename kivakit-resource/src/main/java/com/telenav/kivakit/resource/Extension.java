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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.comparison.Matchable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.codecs.GzipCodec;
import com.telenav.kivakit.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.resource.compression.codecs.ZipCodec;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.collections.list.ObjectList.objectList;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * A {@link FileName} extension, such as ".txt" or ".jar".
 *
 * <p>
 * Common extensions are provided as static constants. An extension can also be constructed with
 * {@link #parseExtension(Listener listener, String)}, with or without a dot prefix:
 * </p>
 *
 * <pre>
 * parseExtension(".xyz");
 * parseExtension("xyz");</pre>
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
 *     <li>{@link #endsWith(Extension)}</li>
 * </ul>
 *
 * <p><b>Tests</b></p>
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
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Extension implements
        Named,
        Matchable<ResourcePathed>,
        Comparable<Extension>
{
    public static final Extension CLASS = parseExtension(".class");

    public static final Extension CSV = parseExtension(".csv");

    @SuppressWarnings("SpellCheckingInspection")
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

    @SuppressWarnings("SpellCheckingInspection")
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

    /**
     * Returns all extensions that are well-known (to KivaKit)
     */
    @SuppressWarnings("DuplicatedCode")
    public static ObjectList<Extension> allWellKnownExtensions()
    {
        return objectList(CLASS, CSV, GEOJSON, GRAPH, GRAPH_GZIP,
                GZIP, JAR, JAVA, JSON, KRYO, MARKDOWN, MD5, OSM, OSMPP,
                OSM_PBF, PBF, PNG, POLY, POM, PROPERTIES, PYTHON, SHA1,
                SHELL, TMP, TXD, TXD_GZIP, TXT, TXT_GZIP, XML, YAML, YML, ZIP).sorted();
    }

    /**
     * Returns extensions that refer to archives
     */
    public static ObjectList<Extension> archiveExtensions()
    {
        return objectList(JAR, ZIP, GZIP);
    }

    /**
     * Returns extensions that refer to executables
     */
    public static ObjectList<Extension> executableExtensions()
    {
        return objectList(PYTHON, SHELL);
    }

    /**
     * Parses the given text into an extension
     *
     * @param listener The listener to call with any problems
     * @param text The text to parse
     * @return The extension
     */
    public static Extension parseExtension(@NotNull Listener listener,
                                           @NotNull String text)
    {
        if (text.matches("(\\.[A-Za-z0-9]+)+"))
        {
            return new Extension(text);
        }
        throw listener.problem("Cannot parse $", text).asException();
    }

    /**
     * Parses the given text into an extension. Throws an exception if parsing fails.
     *
     * @param text The text to parse
     * @return The extension
     */
    public static Extension parseExtension(@NotNull String text)
    {
        return parseExtension(throwingListener(), text);
    }

    /** The extension (without the leading dot) */
    private final String extension;

    protected Extension(@NotNull String value)
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
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull Extension that)
    {
        return name().compareTo(that.name());
    }

    /**
     * True if the given resource has this extension
     */
    public boolean ends(@NotNull Resource resource)
    {
        return resource.hasExtension(this);
    }

    /**
     * True if the given path has this extension
     */
    public boolean ends(@NotNull ResourcePath path)
    {
        return path.extension().equals(this);
    }

    /**
     * True if this extension ends with the given extension. For example the extension ".tar.gz" ends with the extension
     * ".gz"
     */
    public boolean endsWith(@NotNull Extension extension)
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

    /**
     * Returns this extension gzipped
     */
    public Extension gzipped()
    {
        return new Extension(extension + ".gz");
    }

    @Override
    public int hashCode()
    {
        return extension.hashCode();
    }

    /**
     * Returns true if this is an archive extension
     */
    public boolean isArchive()
    {
        return archiveExtensions().contains(this);
    }

    /**
     * Returns true if this is an executable extension
     */
    public boolean isExecutable()
    {
        return executableExtensions().contains(this);
    }

    /**
     * Returns the length of this extension
     */
    public Count length()
    {
        return Count.count(toString().length());
    }

    /**
     * Returns a matcher that matches resources and resource paths with this extension
     */
    @Override
    @NotNull
    public Matcher<ResourcePathed> matcher()
    {
        return resource ->
        {
            var extension = resource.path().compoundExtension();
            return extension != null && extension.endsWith(this);
        };
    }

    /**
     * The extension name (without the dot)
     */
    @Override
    public String name()
    {
        return extension;
    }

    /**
     * Returns this extension with the leading dot, such as ".txt"
     */
    @Override
    public String toString()
    {
        return "." + extension;
    }

    /**
     * Returns this extension with the given extension appended
     */
    public Extension withExtension(@NotNull Extension extension)
    {
        return new Extension(this.extension + extension);
    }
}
