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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.language.object.ObjectPopulator;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.locale.Locale;
import com.telenav.kivakit.core.locale.LocaleLanguage;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.packages.PackagePath;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.filesystem.File.parseFile;
import static com.telenav.kivakit.filesystem.Folder.parseFolder;
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
 *     <li>{@link #propertyMap()} - Creates an empty property map</li>
 *     <li>{@link #propertyMap(VariableMap)} - Creates a property map from the given variable map</li>
 *     <li>{@link #loadPropertyMap(Listener, Resource)} - Loads a property map from the given resource</li>
 *     <li>{@link #loadPropertyMap(Listener, InputStream)} - Loads property map from the given input stream</li>
 *     <li>{@link #loadPropertyMap(Listener, ResourceFolder, String)} - Loads property map from the given package path and relative path</li>
 *     <li>{@link #loadLocalizedPropertyMap(Listener, PackagePath, Locale, LocaleLanguage)}</li>
 * </ul>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #get(String, StringConverter)}</li>
 *     <li>{@link #get(String, StringConverter, Object)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - A copy of this property map using {@link ObjectPopulator}</li>
 *     <li>{@link #asLong(String)} - The given value as a long</li>
 *     <li>{@link #asDouble(String)} - The given value as a double</li>
 *     <li>{@link #asCount(String)} - The given value as a {@link Count}</li>
 *     <li>{@link #asFile(String)}</li>
 *     <li>{@link #asFolder(String)}</li>
 *     <li>{@link #asJson()}</li>
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
 *     <li>{@link #addProperties(Object, PropertyFilter)} - Adds the properties of the given object that match the filter to this map</li>
 * </ul>
 *
 *
 * <p><b>Saving</b></p>
 *
 * <ul>
 *     <li>{@link #save(WritableResource)} - Saves this property map to the given resource</li>
 *     <li>{@link #save(WritableResource, String)} - Saves this property map to the given resource with the given heading</li>
 * </ul>
 *
 * <p><b>Expansion</b></p>
 *
 * <ul>
 *     <li>{@link #expanded()}</li>
 *     <li>{@link #expandedWith(VariableMap)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Locale
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourceType.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class PropertyMap extends VariableMap<String>
{
    /**
     * Loads and returns the property map stored at the given package path. Expansion markers like "${XYZ}" will be
     * expanded using the system properties and environment variables available.
     *
     * @param listener The listener to call with any problems
     * @param path The package path
     * @param locale The KivaKit locale
     * @param languageName The language within the locale
     * @return The loaded property map
     */
    public static PropertyMap loadLocalizedPropertyMap(@NotNull Listener listener,
                                                       @NotNull PackagePath path,
                                                       @NotNull Locale locale,
                                                       @NotNull LocaleLanguage languageName)
    {
        return PropertyMap.loadPropertyMap(listener, packageResource(listener, path, locale.path(languageName).join("/")));
    }

    /**
     * Loads and returns the property map stored in the given folder at the given path. Expansion markers like "${XYZ}"
     * will be expanded using the system properties and environment variables available.
     *
     * @param listener The listener to call with any problems
     * @param folder The folder where the property map is stored
     * @param path The relative path to the property map
     * @return The loaded property map
     */
    public static PropertyMap loadPropertyMap(@NotNull Listener listener,
                                              @NotNull ResourceFolder<?> folder,
                                              @NotNull String path)
    {
        return loadPropertyMap(listener, listener.listenTo(folder.resource(path)));
    }

    /**
     * Loads and returns the property map stored in the given resource. Expansion markers like "${XYZ}" will be expanded
     * using the system properties and environment variables available.
     *
     * @param listener The listener to call with any problems
     * @param resource The resource where the property map is stored
     * @return The loaded property map
     */
    public static PropertyMap loadPropertyMap(@NotNull Listener listener,
                                              @NotNull Resource resource)
    {
        var properties = new PropertyMap();
        var linePattern = Pattern.compile("(?<key>[^=]*?)\\s*=\\s*(?<value>[^=]*)");
        int lineNumber = 1;
        for (var line : resource.reader().readLines(nullProgressReporter()))
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

    /**
     * Loads a property map from the given input stream
     *
     * @param listener The listener to call with problems
     * @param input The input stream to read
     * @return The property map
     */
    public static PropertyMap loadPropertyMap(Listener listener, InputStream input)
    {
        return loadPropertyMap(listener, listener.listenTo(new InputResource(input)));
    }

    /**
     * Returns an empty property map
     */
    public static PropertyMap propertyMap()
    {
        return new PropertyMap();
    }

    /**
     * Returns a property map with the given variables
     *
     * @param variables The variables
     * @return The property map
     */
    public static PropertyMap propertyMap(@NotNull VariableMap<String> variables)
    {
        var map = propertyMap();
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

    /**
     * Adds properties of the given object matching the given filter to this property map
     *
     * @param object The object
     * @param filter The property filter
     */
    public void addProperties(@NotNull Object object,
                              @NotNull PropertyFilter filter)
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
     * Returns the given value as a {@link Folder}
     */
    public File asFile(@NotNull String key)
    {
        return parseFile(this, get(key));
    }

    /**
     * Returns the given value as a {@link Folder}
     */
    public Folder asFolder(@NotNull String key)
    {
        return parseFolder(this, asPathString(key));
    }

    /**
     * Returns this property map as a JSON string
     */
    public String asJson()
    {
        return "{ " + doubleQuoted().join(", ") + " }";
    }

    /**
     * Returns associate a comment with the given key. The comment will be written out when the property map is saved.
     */
    public PropertyMap comment(@NotNull String key,
                               @NotNull String comment)
    {
        comments.put(key, comment);
        return this;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyMap expanded()
    {
        return (PropertyMap) super.expanded();
    }

    /**
     * Returns this property map with all values expanded using the values in the given property map
     */
    public PropertyMap expandedWith(@NotNull VariableMap<String> that)
    {
        var map = new PropertyMap();
        for (var key : keySet())
        {
            var value = get(key);
            map.put(key, that.expand(value));
        }
        return map;
    }

    /**
     * Returns the value for the given key converted with the given converter
     *
     * @param key The key
     * @param converter The converter
     * @return The value
     */
    public <T> T get(@NotNull String key,
                     @NotNull StringConverter<T> converter)
    {
        if (converter.listeners().isEmpty())
        {
            listenTo(converter);
        }
        return converter.convert(get(key));
    }

    /**
     * Returns the value for the given key converted with the given converter, or the default value if there is no value
     * for the key
     *
     * @param key The key
     * @param converter The converter
     * @param defaultValue The default value to use if there is no value for the key
     * @return The value
     */
    public <T> T get(@NotNull String key,
                     @NotNull StringConverter<T> converter,
                     @NotNull T defaultValue)
    {
        var converted = get(key, converter);
        return converted == null ? defaultValue : converted;
    }

    /**
     * Returns this property map as a string joined with the given separator
     *
     * @param separator The separator
     * @return A string with all key/value pairs
     */
    @Override
    public String join(@NotNull String separator)
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

    /**
     * Saves this property map to the given writable resource
     *
     * @param resource The resource
     */
    public void save(@NotNull WritableResource resource)
    {
        save(resource, resource.baseFileName().name());
    }

    /**
     * Saves this property map to the given writable resource with the given heading
     *
     * @param resource The resource
     * @param heading The heading
     */
    public void save(@NotNull WritableResource resource, @NotNull String heading)
    {
        var out = resource.printWriter();
        out.println(AsciiArt.textBox(heading, '#', '#'));
        out.println("");
        out.println(this);
        out.close();
    }

    @Override
    protected VariableMap<String> newStringMap()
    {
        return PropertyMap.propertyMap();
    }
}
