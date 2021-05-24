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

package com.telenav.kivakit.resource.resources.other;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.locales.Locale;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS;

/**
 * A property map is a {@link VariableMap} with strings as both keys and values.
 *
 * <p><b>Creating and Loading Property Maps</b></p>
 *
 * <p>
 * A property map can be created or loaded from a {@link Resource} with:
 * </p>
 *
 * <ul>
 *     <li>{@link #create()} - Creates an empty property map</li>
 *     <li>{@link #of(VariableMap)} - Creates a property map from the given variable map</li>
 *     <li>{@link #load(Resource)} - Loads property map from the given resource</li>
 *     <li>{@link #localized(PackagePath, Locale)} - Loads a property map from the given package with a relative path
 *      from the given {@link Locale} of the form "locales/[language-name](/[country-name])?.</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - A copy of this property map</li>
 *     <li>{@link #asObject(Listener, Class)} - Creates a new instance of the given class and populates its
 *     properties with the values in this property map using {@link ObjectPopulator}</li>
 *     <li>{@link #asInt(String)} - The given value as an int</li>
 *     <li>{@link #asLong(String)} - The given value as a long</li>
 *     <li>{@link #asDouble(String)} - The given value as a double</li>
 *     <li>{@link #asCount(String)} - The given value as a {@link Count}</li>
 * </ul>
 *
 * <p><b>Adding to Property Maps</b></p>
 *
 * <p>
 * In addition to the methods inherited from {@link VariableMap}, property maps add the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #comment(String, String)} - Attaches the given comment to the given key</li>
 *     <li>{@link #add(Object, PropertyFilter)} - Adds the properties of the given object that match the filter to this map</li>
 * </ul>
 *
 *
 * <p><b>Saving Property Maps</b></p>
 *
 * <ul>
 *     <li>{@link #save(WritableResource)} - Saves this property map to the given resource</li>
 *     <li>{@link #save(String, WritableResource)} - Saves this property map to the given resource with the given heading</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Locale
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class PropertyMap extends VariableMap<String>
{
    /**
     * @return An empty property map
     */
    public static PropertyMap create()
    {
        return new PropertyMap();
    }

    public static PropertyMap load(final Resource resource)
    {
        if (resource.exists())
        {
            return load(resource, ProgressReporter.NULL);
        }
        return new PropertyMap();
    }

    public static PropertyMap load(final PackagePath _package, final String path)
    {
        return load(PackageResource.of(_package, FilePath.parseFilePath(path)));
    }

    public static PropertyMap load(final Class<?> _package, final String path)
    {
        return load(PackagePath.packagePath(_package), path);
    }

    public static PropertyMap localized(final PackagePath path, final Locale locale)
    {
        return PropertyMap.load(path, locale.path().join("/"));
    }

    public static PropertyMap of(final VariableMap<String> variables)
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

    /**
     * @return This property map with all values expanded using the values in the given property map
     */
    public PropertyMap expandedWith(final VariableMap<String> that)
    {
        final var map = new PropertyMap();
        for (final var key : keySet())
        {
            final var value = get(key);
            map.put(key, that.expand(value));
        }
        return map;
    }

    /**
     * @return The value of the given key as a {@link Count}
     */
    public Count asCount(final String key)
    {
        return Count.parse(get(key));
    }

    /**
     * @return The value of the given key as a double, or an exception is thrown if the value is invalid or missing
     */
    public double asDouble(final String key)
    {
        return Double.parseDouble(key);
    }

    /**
     * @return The given value as a {@link Folder}
     */
    public File asFile(final String key)
    {
        return File.parse(get(key));
    }

    /**
     * @return The given value as a {@link Folder}
     */
    public Folder asFolder(final String key)
    {
        return Folder.parse(asPath(key));
    }

    /**
     * @return The value of the given key as an integer, or an exception is thrown if the value is invalid or missing
     */
    public int asInt(final String key)
    {
        return Integer.parseInt(get(key));
    }

    /**
     * @return The value of the given key as a long, or an exception is thrown if the value is invalid or missing
     */
    public long asLong(final String key)
    {
        return Long.parseLong(get(key));
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

    /**
     * @return The given key as a path with no trailing slash
     */
    public String asPath(final String key)
    {
        final var value = get(key);
        return value == null ? null : Strip.trailing(value, "/");
    }

    /**
     * @return The given value as a {@link URI}
     */
    public URI asUri(final String key)
    {
        return URI.create(key);
    }

    /**
     * @return Associate a comment with the given key. The comment will be written out when the property map is saved.
     */
    public PropertyMap comment(final String key, final String comment)
    {
        comments.put(key, comment);
        return this;
    }

    @Override
    public PropertyMap copy()
    {
        final var map = new PropertyMap();
        for (final var key : keySet())
        {
            map.put(key, get(key));
        }
        return map;
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
        out.println(this);
        out.close();
    }

    /**
     * @return Loads the given properties resource, interpolating system variables into each value
     */
    private static PropertyMap load(final Resource resource, final ProgressReporter reporter)
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
                    final var value = KivaKit.get().properties().expand(matcher.group("value"));
                    properties.put(key, value);
                }
                else
                {
                    Ensure.fail("Cannot parse line $:$: $", resource.fileName(), lineNumber, line);
                }
            }
            lineNumber++;
        }
        return properties;
    }
}
