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

package com.telenav.kivakit.kernel.language.collections.map.string;

import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.kernel.language.reflection.property.Property;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.CONVERTED_FIELDS_AND_METHODS;

/**
 * A bounded map from string to value which can serve as a {@link PropertyValueSource}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public abstract class BaseStringMap<Value> extends BaseMap<String, Value> implements PropertyValueSource
{
    protected BaseStringMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    protected BaseStringMap(final Maximum maximumSize, final Map<String, Value> map)
    {
        super(maximumSize, map);
    }

    /**
     * @return The value of the given key as a {@link Count}
     */
    public Count asCount(final String key)
    {
        return Count.parse(asString(key));
    }

    /**
     * @return The value of the given key as a double, or an exception is thrown if the value is invalid or missing
     */
    public double asDouble(final String key)
    {
        return Double.parseDouble(key);
    }

    /**
     * @return The value of the given key as an integer, or an exception is thrown if the value is invalid or missing
     */
    public int asInt(final String key)
    {
        return Integer.parseInt(asString(key));
    }

    /**
     * @return The value of the given key as a long, or an exception is thrown if the value is invalid or missing
     */
    public long asLong(final String key)
    {
        return Long.parseLong(asString(key));
    }

    public <T> T asObject(String key, StringConverter<T> converter)
    {
        return converter.convert(asString(key));
    }

    public <T> T asObject(String key, StringConverter<T> converter, T defaultValue)
    {
        var object = asObject(key, converter);
        return object != null ? object : defaultValue;
    }

    public Object asObject(final Listener listener, final Class<?> type)
    {
        try
        {
            final var object = Type.forClass(type).newInstance();
            final var filter = PropertyFilter.kivakitProperties(CONVERTED_FIELDS_AND_METHODS);
            new ObjectPopulator(listener, filter, this).populate(object);
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
        final var value = asString(key);
        return value == null ? null : Strip.trailing(value, "/");
    }

    public String asString(String key)
    {
        return (String) super.get(key);
    }

    public StringList asStringList()
    {
        final var entries = new StringList();
        final var keys = new ArrayList<>(keySet());
        keys.sort(Comparator.naturalOrder());
        for (final var key : keys)
        {
            entries.add(key + " = " + get(key));
        }
        return entries;
    }

    /**
     * @return The given value as a {@link URI}
     */
    public URI asUri(final String key)
    {
        return URI.create(key);
    }

    /**
     * @return The keys and values of this map separated by the given separator
     */
    public String join(final String separator)
    {
        return asStringList().join(separator);
    }

    @Override
    public String toString()
    {
        return join("\n");
    }

    @Override
    public Value valueFor(final Property property)
    {
        final var value = get(property.name());
        if (value != null)
        {
            return value;
        }
        return get(CaseFormat.camelCaseToHyphenated(property.name()));
    }
}
