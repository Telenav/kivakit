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

package com.telenav.kivakit.properties;

import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.language.object.ObjectPopulator;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.locale.Locale;
import com.telenav.kivakit.core.locale.LocaleLanguage;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.packages.PackagePath;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.telenav.kivakit.resource.packages.PackageResource.packageResource;

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
 *     <li>{@link #propertyMap(VariableMap)} - Creates a property map from the given variable map</li>
 *     <li>{@link #load(Listener, Resource)} - Loads a property map from the given resource</li>
 *     <li>{@link #load(Listener, InputStream)} - Loads property map from the given input stream</li>
 *     <li>{@link #load(Listener, ResourceFolder, String)} - Loads property map from the given package path and relative path</li>
 *     <li>{@link #localized(Listener, PackagePath, Locale)} - Loads a property map from the given package with a relative path
 *      from the given {@link Locale} of the form "locales/[language-name](/[country-name])?.</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - A copy of this property map using {@link ObjectPopulator}</li>
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
@SuppressWarnings("unused")
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

    public static PropertyMap load(Listener listener, InputStream input)
    {
        return load(listener, listener.listenTo(new InputResource(input)));
    }

    public static PropertyMap load(Listener listener, ResourceFolder<?> folder, String resourcePath)
    {
        return load(listener, listener.listenTo(folder.resource(resourcePath)));
    }

    /**
     * @return Loads the given .properties resource, interpolating system variables into each value
     */
    public static PropertyMap load(Listener listener, Resource resource)
    {
        var properties = new PropertyMap();
        var linePattern = Pattern.compile("(?<key>[^=]*?)\\s*=\\s*(?<value>[^=]*)");
        int lineNumber = 1;
        for (var line : resource.reader().lines(ProgressReporter.none()))
        {
            var trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#") && !trimmed.startsWith("//"))
            {
                var matcher = linePattern.matcher(line);
                if (matcher.matches())
                {
                    var key = matcher.group("key");
                    var value = matcher.group("value");
                    properties.put(key, value);
                }
                else
                {
                    listener.problem("Cannot parse line $:$: $", resource.fileName(), lineNumber, line);
                }
            }
            lineNumber++;
        }
        return properties;
    }

    public static PropertyMap localized(Listener listener, PackagePath path, Locale locale, LocaleLanguage languageName)
    {
        return PropertyMap.load(listener, packageResource(listener, path, locale.path(languageName).join("/")));
    }

    public static PropertyMap propertyMap(VariableMap<String> variables)
    {
        var map = create();
        for (var key : variables.keySet())
        {
            map.put(key, variables.get(key));
        }
        return map;
    }

    private final Map<String, String> comments = new HashMap<>();

    public PropertyMap()
    {
    }

    public void add(Object object, PropertyFilter filter)
    {
        Type<?> type = Type.type(object);
        for (var property : type.properties(filter))
        {
            if (!"class".equals(property.name()))
            {
                var getter = property.getter();
                if (getter != null)
                {
                    var value = getter.get(object);
                    if (value != null)
                    {
                        put(property.name(), value.toString());
                    }
                }
            }
        }
    }

    /**
     * @return The given value as a {@link Folder}
     */
    public File asFile(String key)
    {
        return File.parseFile(this, get(key));
    }

    /**
     * @return The given value as a {@link Folder}
     */
    public Folder asFolder(String key)
    {
        return Folder.parseFolder(this, asPathString(key));
    }

    /**
     * @return This property map as a JSON string
     */
    public String asJson()
    {
        return "{ " + doubleQuoted().join(", ") + " }";
    }

    /**
     * @return Associate a comment with the given key. The comment will be written out when the property map is saved.
     */
    public PropertyMap comment(String key, String comment)
    {
        comments.put(key, comment);
        return this;
    }

    @Override
    public PropertyMap copy()
    {
        var map = new PropertyMap();
        for (var key : keySet())
        {
            map.put(key, get(key));
        }
        return map;
    }

    @Override
    public PropertyMap expanded()
    {
        return (PropertyMap) super.expanded();
    }

    /**
     * @return This property map with all values expanded using the values in the given property map
     */
    public PropertyMap expandedWith(VariableMap<String> that)
    {
        var map = new PropertyMap();
        for (var key : keySet())
        {
            var value = get(key);
            map.put(key, that.expand(value));
        }
        return map;
    }

    public <T> T get(String key, StringConverter<T> converter)
    {
        if (converter.listeners().isEmpty())
        {
            listenTo(converter);
        }
        return converter.convert(get(key));
    }

    public <T> T get(String key, StringConverter<T> converter, T defaultValue)
    {
        var converted = get(key, converter);
        return converted == null ? defaultValue : converted;
    }

    @Override
    public String join(String separator)
    {
        var entries = new StringList();
        var keys = new ArrayList<>(keySet());
        keys.sort(Comparator.naturalOrder());
        for (var key : keys)
        {
            var comment = comments.get(key);
            if (comment != null)
            {
                entries.add("");
                entries.add("# " + comment);
            }
            entries.add(key + " = " + get(key));
        }
        return entries.join(separator);
    }

    public void save(WritableResource resource)
    {
        save(resource.baseFileName().name(), resource);
    }

    public void save(String heading, WritableResource resource)
    {
        var out = resource.printWriter();
        out.println(AsciiArt.box(heading, '#', '#'));
        out.println("");
        out.println(this);
        out.close();
    }

    @Override
    protected VariableMap<String> newStringMap()
    {
        return PropertyMap.create();
    }
}
