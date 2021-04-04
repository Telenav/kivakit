////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.other;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.language.locales.Locale;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.primitives.Ints;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS;

@UmlClassDiagram(diagram = DiagramResourceType.class)
public class PropertyMap extends VariableMap<String>
{
    public static PropertyMap create()
    {
        return new PropertyMap();
    }

    public static PropertyMap load(final PackagePath _package, final String path)
    {
        return load(PackageResource.packageResource(_package, FilePath.parseFilePath(path)));
    }

    /**
     * @return Loads the given properties resource, interpolating system variables into each value
     */
    public static PropertyMap load(final Resource resource, final ProgressReporter reporter)
    {
        final var properties = new PropertyMap();
        final var linePattern = Pattern.compile("(?<key>[^=]*?)\\s*=\\s*(?<value>[^=]*)");
        int lineNumber = 1;
        for (final var line : resource.reader().lines(reporter))
        {
            final var trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#") && !trimmed.startsWith("//"))
            {
                final var matcher = linePattern.matcher(line);
                if (matcher.matches())
                {
                    final var key = matcher.group("key");
                    final var value = KivaKit.get().properties().expanded(matcher.group("value"));
                    properties.put(key, value);
                }
                else
                {
                    fail("Cannot parse line $:$: $", resource.fileName(), lineNumber, line);
                }
            }
            lineNumber++;
        }
        return properties;
    }

    public static PropertyMap load(final Resource resource)
    {
        if (resource.exists())
        {
            return load(resource, ProgressReporter.NULL);
        }
        return new PropertyMap();
    }

    public static PropertyMap localized(final PackagePath path, final Locale locale)
    {
        return PropertyMap.load(path, locale.path().join("/"));
    }

    public static PropertyMap propertyMap(final VariableMap<String> variables)
    {
        final var map = create();
        for (final var key : variables.keySet())
        {
            map.put(key, variables.get(key));
        }
        return map;
    }

    private final Map<String, String> comments = new HashMap<>();

    protected PropertyMap()
    {
    }

    public void add(final Object object, final PropertyFilter filter)
    {
        final Type<?> type = Type.of(object);
        for (final var property : type.properties(filter))
        {
            if (!"class".equals(property.name()))
            {
                final var getter = property.getter();
                if (getter != null)
                {
                    final var value = getter.get(object);
                    if (value != null)
                    {
                        put(property.name(), value.toString());
                    }
                }
            }
        }
    }

    public int asInt(final String key)
    {
        return Ints.parse(get(key), Integer.MIN_VALUE);
    }

    public Object asObject(final Listener listener, final Class<?> type)
    {
        try
        {
            final var object = Type.forClass(type).newInstance();
            new ObjectPopulator(listener, INCLUDED_PROPERTIES_AND_FIELDS, this).populate(object);
            return object;
        }
        catch (final Exception e)
        {
            listener.receive(new Problem(e, "Unable to convert $", type));
            return null;
        }
    }

    public VariableMap<String> asVariableMap()
    {
        final var map = new VariableMap<String>();
        for (final var key : keySet())
        {
            map.put(key, get(key));
        }
        return map;
    }

    public PropertyMap comment(final String key, final String comment)
    {
        comments.put(key, comment);
        return this;
    }

    public Count count(final String key)
    {
        return Count.parse(get(key));
    }

    @Override
    public String join(final String separator)
    {
        final var entries = new StringList();
        final var keys = new ArrayList<>(keySet());
        keys.sort(Comparator.naturalOrder());
        for (final var key : keys)
        {
            final var comment = comments.get(key);
            if (comment != null)
            {
                entries.add("");
                entries.add("# " + comment);
            }
            entries.add(key + " = " + get(key));
        }
        return entries.join(separator);
    }

    public void save(final WritableResource resource)
    {
        save(resource.baseName().name(), resource);
    }

    public void save(final String heading, final WritableResource resource)
    {
        final var out = resource.printWriter();
        out.println(AsciiArt.box(heading, '#', '#'));
        out.println("");
        out.println(toString());
        out.close();
    }
}
