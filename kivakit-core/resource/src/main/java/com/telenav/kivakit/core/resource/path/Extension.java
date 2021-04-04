////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.path;

import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.compression.Codec;
import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.resource.compression.codecs.GzipCodec;
import com.telenav.kivakit.core.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourcePath;

import java.util.ArrayList;
import java.util.List;

@UmlClassDiagram(diagram = DiagramResourcePath.class)
public class Extension implements Named
{
    // Archives
    public static final Extension GZIP = new Extension(".gz");

    public static final Extension ZIP = new Extension(".zip");

    public static final Extension JAR = new Extension(".jar");

    public static final Extension KRYO = new Extension(".kryo");

    // Executable files
    public static final Extension PYTHON = new Extension(".py");

    public static final Extension SHELL = new Extension(".sh");

    // General file formats
    public static final Extension PROPERTIES = new Extension(".properties");

    public static final Extension TXT = new Extension(".txt");

    public static final Extension CSV = new Extension(".csv");

    public static final Extension PNG = new Extension(".png");

    public static final Extension TMP = new Extension(".tmp");

    // Map file formats
    public static final Extension TXD = new Extension(".txd");

    public static final Extension GRAPH = new Extension(".graph");

    public static final Extension PBF = new Extension(".pbf");

    public static final Extension OSM = new Extension(".osm");

    public static final Extension OSM_PBF = new Extension(".osm.pbf");

    public static final Extension POLY = new Extension(".poly");

    public static final Extension GEOJSON = new Extension(".geojson");

    public static final Extension OSMPP = new Extension(".osmpp");

    // Compressed formats
    public static final Extension TXT_GZIP = TXT.gzipped();

    public static final Extension TXD_GZIP = TXD.gzipped();

    public static final Extension GRAPH_GZIP = GRAPH.gzipped();

    public static final Extension JAVA = new Extension(".java");

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
        return null;
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

    public Codec codec()
    {
        if (endsWith(GZIP))
        {
            return new GzipCodec();
        }
        return new NullCodec();
    }

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

    public Matcher<File> matcher()
    {
        return file ->
        {
            final var extension = file.compoundExtension();
            return extension != null && extension.endsWith(this);
        };
    }

    /**
     *
     */
    public boolean matches(final Resource resource)
    {
        return resource.hasExtension(this);
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
